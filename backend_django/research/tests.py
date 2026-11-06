from django.test import TestCase
from rest_framework.test import APIClient
from rest_framework_simplejwt.tokens import AccessToken
from unittest.mock import patch
from django.contrib.auth import get_user_model

from accounts.models import AccountProfile
from research.models import (
    ResearchJob,
    ResearchRecommendation,
    ResearchReport,
    ResearchSource,
)
from research.services.gemini_service import GeminiServiceError
from research.services.research_generation_service import (
    ResearchGenerationError,
    ResearchGenerationService,
)


def valid_input():
    return {
        "budget_kes": 45000,
        "skills": ["baking", "sales"],
        "interests": ["food", "local trade"],
        "county": "Nakuru",
        "available_equipment": ["oven", "smartphone"],
        "available_time": "20 hours per week",
        "preferred_business_category": "Food",
    }


def generated_report():
    return {
        "title": "Small batch bakery research",
        "summary": "A low-capital bakery concept to validate locally.",
        "sector": "Food",
        "market_demand_analysis": "Demand should be checked with local buyers.",
        "target_customers": ["Nearby households", "Small offices"],
        "competitor_analysis": ["Compare nearby bakeries and kiosks in person."],
        "startup_requirements": [
            {"name": "Initial ingredients", "cost_min_kes": 5000, "cost_max_kes": 8000}
        ],
        "revenue_opportunities": ["Preordered bread and snack boxes"],
        "marketing_strategies": ["Offer samples to nearby offices."],
        "risks_and_challenges": ["Ingredient price changes", "Unsold stock"],
        "practical_launch_steps": ["Interview ten potential buyers."],
        "assumptions": ["An oven is available and usable."],
        "confidence_level": "MEDIUM",
        "verified_facts": [{"fact": "fabricated"}],
        "sources": [
            {"title": "fabricated", "url": "https://example.com/fabricated"}
        ],
        "opportunities": [
            {
                "title": "Preorder bakery",
                "category": "Food",
                "description": "Sell baked goods made to customer orders.",
                "market_demand_analysis": "Validate interest with sample orders.",
                "target_customers": ["Households"],
                "competitor_analysis": ["Nearby bakeries"],
                "startup_requirements": [
                    {"name": "Ingredients", "cost_min_kes": 5000, "cost_max_kes": 8000}
                ],
                "revenue_opportunities": ["Preorders"],
                "marketing_strategies": ["WhatsApp catalogue"],
                "risks_and_challenges": ["Food spoilage"],
                "practical_launch_steps": ["Test three products."],
                "assumptions": ["Oven is available."],
                "confidence_level": "MEDIUM",
                "confidence_score": 65,
                "estimated_startup_min_kes": 15000,
                "estimated_startup_max_kes": 30000,
            }
        ],
    }


class FakeGeminiService:
    def __init__(self, result=None, error=None):
        self.result = result or generated_report()
        self.error = error

    def generate_json(self, prompt, system_instruction=None):
        if self.error:
            raise self.error
        return self.result


class ResearchGenerationTests(TestCase):
    @classmethod
    def setUpTestData(cls):
        User = get_user_model()
        cls.user = User.objects.create_user(
            username="researcher@example.com",
            email="researcher@example.com",
            password="Sufficiently-Strong-Password-2026!",
        )
        AccountProfile.objects.create(user=cls.user, email_verified=True)

    def test_generation_saves_report_recommendations_and_honest_provenance(self):
        job, report = ResearchGenerationService(
            gemini_service=FakeGeminiService()
        ).generate_personalized(self.user, "device-123", valid_input())

        self.assertEqual(job.status, "COMPLETED")
        self.assertEqual(job.job_type, "PERSONALIZED")
        self.assertEqual(job.input_data["budget_kes"], 45000)
        self.assertEqual(ResearchReport.objects.count(), 1)
        self.assertEqual(ResearchRecommendation.objects.count(), 1)
        self.assertEqual(ResearchSource.objects.count(), 0)
        self.assertEqual(report.report_data["verified_facts"], [])
        self.assertEqual(report.report_data["sources"], [])
        self.assertEqual(
            report.report_data["analysis_provenance"], "GENERATED_ANALYSIS"
        )
        self.assertEqual(
            report.report_data["opportunities"][0]["estimates_classification"],
            "ESTIMATED_NOT_QUOTED",
        )
        recommendation = report.recommendations.get()
        self.assertEqual(recommendation.estimated_startup_min_kes, 15000)
        self.assertEqual(recommendation.confidence_score, 65)

    def test_provider_failure_marks_job_failed_without_provider_details(self):
        with self.assertRaises(ResearchGenerationError) as caught:
            ResearchGenerationService(
                gemini_service=FakeGeminiService(
                    error=GeminiServiceError("secret-key-must-not-leak")
                )
            ).generate_personalized(self.user, "device-123", valid_input())

        job = ResearchJob.objects.get(device_id="device-123", user=self.user)
        self.assertEqual(job.status, "FAILED")
        self.assertNotIn("secret-key-must-not-leak", job.error_message)
        self.assertNotIn("secret-key-must-not-leak", str(caught.exception))
        self.assertEqual(caught.exception.http_status, 502)
        self.assertEqual(ResearchReport.objects.count(), 0)

    def test_personalized_endpoint_rejects_invalid_input_before_creating_job(self):
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f"Bearer {AccessToken.for_user(self.user)}")
        response = client.post(
            "/api/v1/research/personalised/",
            {"device_id": "device-123", "budget_kes": -1},
            format="json",
        )

        self.assertEqual(response.status_code, 400)
        self.assertEqual(ResearchJob.objects.count(), 0)

    def test_status_and_completed_report_are_scoped_to_authenticated_user(self):
        User = get_user_model()
        other_user = User.objects.create_user(
            username="another@example.com",
            email="another@example.com",
            password="Sufficiently-Strong-Password-2026!",
        )
        AccountProfile.objects.create(user=other_user, email_verified=True)
        job = ResearchJob.objects.create(
            job_type="PERSONALIZED",
            status="COMPLETED",
            user=other_user,
            device_id="device-123",
            input_data=valid_input(),
        )
        report = ResearchReport.objects.create(
            job=job,
            title="Report",
            summary="Summary",
            report_data={"sources": [], "opportunities": []},
            county="Nakuru",
            sector="Food",
        )
        client = APIClient()

        client.credentials(HTTP_AUTHORIZATION=f"Bearer {AccessToken.for_user(self.user)}")
        status_response = client.get(f"/api/v1/research/jobs/{job.id}/")
        report_response = client.get(f"/api/v1/research/jobs/{job.id}/report/")
        self.assertEqual(status_response.status_code, 404)
        self.assertEqual(report_response.status_code, 404)

    @patch("research.views.ResearchGenerationService")
    def test_authenticated_personalized_endpoint_creates_owned_job(self, service_class):
        service = ResearchGenerationService(gemini_service=FakeGeminiService())
        service_class.return_value = service
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f"Bearer {AccessToken.for_user(self.user)}")
        response = client.post(
            "/api/v1/research/personalised/",
            {"device_id": "device-123", **valid_input()},
            format="json",
        )

        self.assertEqual(response.status_code, 201)
        job = ResearchJob.objects.get(id=response.data["job_id"])
        self.assertEqual(job.user, self.user)
        status_response = client.get(f"/api/v1/research/jobs/{job.id}/")
        report_response = client.get(f"/api/v1/research/jobs/{job.id}/report/")
        self.assertEqual(status_response.status_code, 200)
        self.assertEqual(status_response.data["status"], "COMPLETED")
        self.assertEqual(report_response.status_code, 200)
        self.assertEqual(report_response.data["job_id"], str(job.id))

