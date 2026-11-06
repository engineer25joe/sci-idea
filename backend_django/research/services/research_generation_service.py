import json

from django.db import transaction
from django.utils import timezone

from research.models import (
    ResearchJob,
    ResearchRecommendation,
    ResearchReport,
)
from research.services.gemini_service import (
    GeminiConfigurationError,
    GeminiRetriesExhaustedError,
    GeminiService,
    GeminiServiceError,
)


REPORT_SYSTEM_INSTRUCTION = """
You prepare practical small-business research for people in Kenya. Return only
one valid JSON object matching the requested schema. Be specific to the user's
budget, skills, interests, county, equipment, time, and preferred category.
Never invent market statistics, competitor names, citations, sources, URLs,
licenses, prices, or facts. No browsing or independent source verification is
available: set verified_facts and sources to empty arrays. Present uncertain
market observations as analysis, all costs and revenue figures as estimates,
and state assumptions. Do not imply estimates are quotes or guaranteed income.
Use KES for costs and describe the basis/uncertainty where possible.
""".strip()


class ResearchGenerationError(Exception):
    def __init__(self, job, message, http_status):
        super().__init__(message)
        self.job = job
        self.http_status = http_status


class InvalidGeneratedReport(Exception):
    pass


def _require_text(data, key, *, max_length=None):
    value = data.get(key)
    if not isinstance(value, str) or not value.strip():
        raise InvalidGeneratedReport(f"Missing or invalid {key}.")
    value = value.strip()
    if max_length:
        value = value[:max_length]
    return value


def _require_text_list(data, key):
    value = data.get(key)
    if not isinstance(value, list) or any(
        not isinstance(item, str) or not item.strip() for item in value
    ):
        raise InvalidGeneratedReport(f"Missing or invalid {key}.")
    return [item.strip() for item in value]


def _optional_cost(value, field_name):
    if value is None:
        return None
    if isinstance(value, bool) or not isinstance(value, int) or value < 0:
        raise InvalidGeneratedReport(f"Invalid estimated cost in {field_name}.")
    return value


def _normalize_cost_items(value, field_name):
    if not isinstance(value, list):
        raise InvalidGeneratedReport(f"Invalid {field_name}.")
    normalized = []
    for item in value:
        if not isinstance(item, dict):
            raise InvalidGeneratedReport(f"Invalid {field_name} item.")
        name = _require_text(item, "name", max_length=250)
        minimum = _optional_cost(item.get("cost_min_kes"), field_name)
        maximum = _optional_cost(item.get("cost_max_kes"), field_name)
        if minimum is not None and maximum is not None and minimum > maximum:
            raise InvalidGeneratedReport(f"Invalid cost range in {field_name}.")
        normalized.append(
            {
                "name": name,
                "cost_min_kes": minimum,
                "cost_max_kes": maximum,
                "classification": "ESTIMATED_NOT_QUOTED",
            }
        )
    return normalized


def normalize_generated_report(data, county, preferred_category):
    if not isinstance(data, dict):
        raise InvalidGeneratedReport("The generated report must be a JSON object.")

    opportunities = data.get("opportunities")
    if not isinstance(opportunities, list) or not opportunities:
        raise InvalidGeneratedReport("At least one opportunity is required.")

    normalized_opportunities = []
    for opportunity in opportunities[:8]:
        if not isinstance(opportunity, dict):
            raise InvalidGeneratedReport("Invalid opportunity entry.")
        minimum = _optional_cost(
            opportunity.get("estimated_startup_min_kes"), "startup estimate"
        )
        maximum = _optional_cost(
            opportunity.get("estimated_startup_max_kes"), "startup estimate"
        )
        if minimum is not None and maximum is not None and minimum > maximum:
            raise InvalidGeneratedReport("Invalid opportunity startup cost range.")
        confidence = opportunity.get("confidence_score")
        if confidence is not None and (
            isinstance(confidence, bool)
            or not isinstance(confidence, int)
            or not 0 <= confidence <= 100
        ):
            raise InvalidGeneratedReport("Invalid confidence score.")
        confidence_level = opportunity.get("confidence_level")
        if confidence_level not in {"LOW", "MEDIUM", "HIGH"}:
            raise InvalidGeneratedReport("Invalid confidence level.")

        normalized_opportunities.append(
            {
                "title": _require_text(opportunity, "title", max_length=250),
                "category": (
                    opportunity.get("category", preferred_category) or preferred_category
                )[:100],
                "description": _require_text(opportunity, "description"),
                "market_demand_analysis": _require_text(
                    opportunity, "market_demand_analysis"
                ),
                "target_customers": _require_text_list(
                    opportunity, "target_customers"
                ),
                "competitor_analysis": _require_text_list(
                    opportunity, "competitor_analysis"
                ),
                "startup_requirements": _normalize_cost_items(
                    opportunity.get("startup_requirements"), "startup_requirements"
                ),
                "revenue_opportunities": _require_text_list(
                    opportunity, "revenue_opportunities"
                ),
                "marketing_strategies": _require_text_list(
                    opportunity, "marketing_strategies"
                ),
                "risks_and_challenges": _require_text_list(
                    opportunity, "risks_and_challenges"
                ),
                "practical_launch_steps": _require_text_list(
                    opportunity, "practical_launch_steps"
                ),
                "assumptions": _require_text_list(opportunity, "assumptions"),
                "confidence_level": confidence_level,
                "confidence_score": confidence,
                "estimated_startup_min_kes": minimum,
                "estimated_startup_max_kes": maximum,
                "estimates_classification": "ESTIMATED_NOT_QUOTED",
            }
        )

    report = {
        "title": _require_text(data, "title", max_length=250),
        "summary": _require_text(data, "summary"),
        "county": county,
        "sector": (data.get("sector") or preferred_category or "General")[:100],
        "market_demand_analysis": _require_text(data, "market_demand_analysis"),
        "target_customers": _require_text_list(data, "target_customers"),
        "competitor_analysis": _require_text_list(data, "competitor_analysis"),
        "startup_requirements": _normalize_cost_items(
            data.get("startup_requirements"), "startup_requirements"
        ),
        "revenue_opportunities": _require_text_list(data, "revenue_opportunities"),
        "marketing_strategies": _require_text_list(data, "marketing_strategies"),
        "risks_and_challenges": _require_text_list(data, "risks_and_challenges"),
        "practical_launch_steps": _require_text_list(data, "practical_launch_steps"),
        "assumptions": _require_text_list(data, "assumptions"),
        "confidence_level": data.get("confidence_level"),
        "opportunities": normalized_opportunities,
        # There is no independent web verification in this service. Ignore
        # model-provided citations and facts instead of persisting inventions.
        "verified_facts": [],
        "sources": [],
        "source_verification_status": "NO_SOURCES_INDEPENDENTLY_VERIFIED",
        "analysis_provenance": "GENERATED_ANALYSIS",
        "estimates_classification": "ESTIMATED_NOT_QUOTED",
    }
    if report["confidence_level"] not in {"LOW", "MEDIUM", "HIGH"}:
        raise InvalidGeneratedReport("Invalid report confidence level.")
    return report


def _build_prompt(input_data):
    return """Create a personalized business research report using this input:
{input_json}

Return a JSON object with exactly these top-level fields:
- title (string), summary (string), sector (string)
- market_demand_analysis (string)
- target_customers (array of strings)
- competitor_analysis (array of strings; describe competitor types if names are not known)
- startup_requirements (array of objects: name, cost_min_kes, cost_max_kes; costs may be null)
- revenue_opportunities (array of strings; do not promise revenue)
- marketing_strategies (array of strings)
- risks_and_challenges (array of strings)
- practical_launch_steps (array of strings)
- assumptions (array of strings)
- confidence_level (LOW, MEDIUM, or HIGH)
- opportunities (array of 1 to 8 objects, each with: title, category, description,
  market_demand_analysis, target_customers, competitor_analysis,
  startup_requirements (same item schema), revenue_opportunities,
  marketing_strategies, risks_and_challenges, practical_launch_steps,
  assumptions, confidence_level, confidence_score (0-100),
  estimated_startup_min_kes, estimated_startup_max_kes)
- verified_facts (empty array) and sources (empty array)

Use null when an estimate cannot be reasonably bounded. Do not return sources,
URLs, specific competitor names, or verified facts because no source verification
is available. Clearly label uncertainty in the analysis and assumptions.""".format(
        input_json=json.dumps(input_data, ensure_ascii=False)
    )


class ResearchGenerationService:
    def __init__(self, gemini_service=None):
        self.gemini_service = gemini_service

    def generate_personalized(self, user, device_id, input_data):
        job = ResearchJob.objects.create(
            job_type="PERSONALIZED",
            status="PENDING",
            user=user,
            device_id=device_id,
            input_data=input_data,
        )
        job.status = "PROCESSING"
        job.started_at = timezone.now()
        job.save(update_fields=["status", "started_at"])

        try:
            gemini_service = self.gemini_service or GeminiService()
            generated = gemini_service.generate_json(
                prompt=_build_prompt(input_data),
                system_instruction=REPORT_SYSTEM_INSTRUCTION,
            )
            report_data = normalize_generated_report(
                generated,
                county=input_data["county"],
                preferred_category=input_data.get("preferred_business_category", ""),
            )
            with transaction.atomic():
                report = ResearchReport.objects.create(
                    job=job,
                    title=report_data["title"],
                    summary=report_data["summary"],
                    report_data=report_data,
                    county=report_data["county"],
                    sector=report_data["sector"],
                )
                for opportunity in report_data["opportunities"]:
                    ResearchRecommendation.objects.create(
                        report=report,
                        title=opportunity["title"],
                        category=opportunity["category"],
                        description=opportunity["description"],
                        estimated_startup_min_kes=opportunity[
                            "estimated_startup_min_kes"
                        ],
                        estimated_startup_max_kes=opportunity[
                            "estimated_startup_max_kes"
                        ],
                        confidence_score=opportunity["confidence_score"],
                        assumptions=opportunity["assumptions"],
                        risks=opportunity["risks_and_challenges"],
                    )
                job.status = "COMPLETED"
                job.completed_at = timezone.now()
                job.error_message = ""
                job.save(
                    update_fields=["status", "completed_at", "error_message"]
                )
            return job, report
        except Exception as exc:
            if isinstance(exc, GeminiRetriesExhaustedError):
                job.error_message = "Temporary research service issue; try again later."
                http_status = 503
            elif isinstance(exc, GeminiConfigurationError):
                job.error_message = "Research service configuration issue."
                http_status = 503
            elif isinstance(exc, (GeminiServiceError, InvalidGeneratedReport)):
                job.error_message = "Research could not be generated from the provider response."
                http_status = 502
            else:
                job.error_message = "Research generation failed due to a service error."
                http_status = 502
            job.status = "FAILED"
            job.completed_at = timezone.now()
            job.save(update_fields=["status", "completed_at", "error_message"])
            raise ResearchGenerationError(
                job=job,
                message=job.error_message,
                http_status=http_status,
            ) from exc
