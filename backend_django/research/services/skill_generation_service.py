import re
import unicodedata
from datetime import timedelta

from django.db import transaction
from django.utils import timezone

from api.models import Skill, SkillCategory
from research.services.gemini_service import (
    GeminiService,
    GeminiServiceError,
)


SYSTEM_INSTRUCTION = """
You are SCI IDEA's autonomous skills research assistant.

Research practical, marketable skills suitable for young entrepreneurs,
especially in Kenya and other African markets.

Use Google Search grounding when available.
Prefer practical skills that can generate legitimate income or employment.

Never invent statistics, sources, qualifications or guaranteed earnings.
Return valid JSON only.
Generate exactly 10 distinct skills.
Each skill must have a concise, useful description.
"""


def create_skill_id(name):
    value = unicodedata.normalize("NFKD", name)
    value = value.encode("ascii", "ignore").decode("ascii").lower()
    value = re.sub(r"[^a-z0-9]+", "_", value).strip("_")
    return value[:90] or "skill"


class SkillGenerationService:

    def __init__(self, gemini_service=None):
        self.gemini = gemini_service or GeminiService()

    def generate(self):
        categories = list(
            SkillCategory.objects.filter(is_active=True)
        )

        if not categories:
            raise GeminiServiceError(
                "No active skill categories are available."
            )

        category_data = [
            {
                "code": category.code,
                "name": category.name,
            }
            for category in categories
        ]

        prompt = f"""
Research and generate exactly 10 NEW practical skills for SCI IDEA.

Available categories:
{category_data}

Return this JSON structure:

{{
  "skills": [
    {{
      "name": "Skill name",
      "category_code": "DIGITAL_TECH",
      "description": "Detailed practical description."
    }}
  ]
}}

Requirements:
- Exactly 10 skills.
- Use only the supplied category codes.
- Avoid duplicates and near-duplicates.
- Include digital, vocational, agricultural and commercial opportunities
  where appropriate.
- Skills must be teachable and practically useful.
- Descriptions must explain what the person can actually do.
- Do not invent certifications or guaranteed earnings.
- Research current practical opportunities using Google Search.
- Prioritize credible sources relevant to Kenya and Africa.
- Base descriptions on researched information.
- Do not fabricate source URLs.
"""

        result = self.gemini.generate_json_with_search(
            prompt=prompt,
            system_instruction=SYSTEM_INSTRUCTION,
        )

        research_sources = result.pop("_grounding_sources", [])

        if not research_sources:
            raise GeminiServiceError(
                "No Google Search sources were returned. "
                "Skills cannot be submitted without research references."
            )
        generated = result.get("skills")

        if not isinstance(generated, list) or len(generated) != 10:
            raise GeminiServiceError(
                "Gemini did not return exactly 10 skills."
            )

        category_map = {
            category.code: category
            for category in categories
        }

        existing_names = {
            name.casefold()
            for name in Skill.objects.values_list("name", flat=True)
        }

        seen_names = set()
        prepared = []

        for item in generated:
            if not isinstance(item, dict):
                raise GeminiServiceError(
                    "Invalid generated skill."
                )

            name = item.get("name")
            description = item.get("description")
            category_code = item.get("category_code")

            if not all(
                isinstance(value, str) and value.strip()
                for value in (name, description, category_code)
            ):
                raise GeminiServiceError(
                    "Generated skill has missing fields."
                )

            name = name.strip()
            description = description.strip()
            category_code = category_code.strip()

            if len(name) > 150:
                raise GeminiServiceError(
                    "Generated skill name is too long."
                )

            normalized_name = name.casefold()

            if normalized_name in existing_names or normalized_name in seen_names:
                raise GeminiServiceError(
                    f"Duplicate skill detected: {name}"
                )

            if category_code not in category_map:
                raise GeminiServiceError(
                    f"Invalid skill category: {category_code}"
                )

            seen_names.add(normalized_name)

            prepared.append({
                "name": name,
                "description": description,
                "category": category_map[category_code],
            })

        now = timezone.now()
        deadline = now + timedelta(hours=24)

        created = []

        with transaction.atomic():
            existing_ids = set(
                Skill.objects.values_list("id", flat=True)
            )

            for item in prepared:
                base_id = create_skill_id(item["name"])
                skill_id = base_id
                suffix = 2

                while skill_id in existing_ids:
                    skill_id = f"{base_id}_{suffix}"
                    suffix += 1

                existing_ids.add(skill_id)

                skill = Skill.objects.create(
                    id=skill_id,
                    name=item["name"],
                    description=item["description"],
                    category=item["category"],
                    is_active=False,
                    review_status="PENDING",
                    submitted_for_review_at=now,
                    auto_publish_at=deadline,
                    research_sources=research_sources,
                )

                created.append(skill)

        return created