import os
import secrets

from rest_framework import status
from rest_framework.permissions import IsAdminUser
from rest_framework.response import Response
from rest_framework.views import APIView
from research.services.gemini_service import GeminiServiceError
from research.services.skill_generation_service import SkillGenerationService
from django.db import transaction
from django.utils import timezone
from rest_framework.permissions import AllowAny
from rest_framework import status
from api.models import Skill


class AutomatedSkillPublishingView(APIView):
    authentication_classes = []
    permission_classes = [AllowAny]

    def post(self, request):
        configured_token = os.environ.get("PUBLISH_TRIGGER_TOKEN")

        if not configured_token:
            return Response(
                {"success": False, "message": "Publishing trigger not configured."},
                status=status.HTTP_503_SERVICE_UNAVAILABLE,
            )

        supplied_token = request.headers.get("X-Publish-Token", "")

        if not secrets.compare_digest(supplied_token, configured_token):
            return Response(
                {"success": False, "message": "Unauthorized."},
                status=status.HTTP_403_FORBIDDEN,
            )

        now = timezone.now()

        with transaction.atomic():
            due_skills = Skill.objects.filter(
                review_status="PENDING",
                is_active=False,
                auto_publish_at__isnull=False,
                auto_publish_at__lte=now,
            )

            published_count = due_skills.update(
                review_status="APPROVED",
                is_active=True,
                reviewed_at=now,
                auto_publish_at=None,
            )

        return Response({
            "success": True,
            "published": published_count,
            "executed_at": now.isoformat(),
        })

class SkillGenerationView(APIView):
    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            skills = SkillGenerationService().generate()

            return Response(
                {
                    "success": True,
                    "message": f"{len(skills)} skills generated successfully.",
                    "skills": [
                        {
                            "id": skill.id,
                            "name": skill.name,
                            "category": skill.category.code,
                            "review_status": skill.review_status,
                            "auto_publish_at": skill.auto_publish_at,
                        }
                        for skill in skills
                    ],
                },
                status=status.HTTP_201_CREATED,
            )

        except GeminiServiceError as exc:
            return Response(
                {
                    "success": False,
                    "message": str(exc),
                },
                status=status.HTTP_502_BAD_GATEWAY,
            )

        except Exception:
            return Response(
                {
                    "success": False,
                    "message": "Skill generation failed. Check the server logs.",
                },
                status=status.HTTP_500_INTERNAL_SERVER_ERROR,
            )

class AutomatedSkillPublishingView(APIView):
    authentication_classes = []
    permission_classes = [AllowAny]

    def post(self, request):
        configured_token = os.environ.get("PUBLISH_TRIGGER_TOKEN")

        if not configured_token:
            return Response(
                {"success": False, "message": "Publishing is not configured."},
                status=status.HTTP_503_SERVICE_UNAVAILABLE,
            )

        supplied_token = request.headers.get("X-Publish-Token", "")

        if not secrets.compare_digest(supplied_token, configured_token):
            return Response(
                {"success": False, "message": "Unauthorized."},
                status=status.HTTP_403_FORBIDDEN,
            )

        now = timezone.now()

        with transaction.atomic():
            due_skills = Skill.objects.filter(
                review_status="PENDING",
                is_active=False,
                auto_publish_at__isnull=False,
                auto_publish_at__lte=now,
            )

            published_count = due_skills.update(
                review_status="APPROVED",
                is_active=True,
                reviewed_at=now,
                auto_publish_at=None,
            )

        return Response({
            "success": True,
            "published": published_count,
            "executed_at": now.isoformat(),
        })