from django.shortcuts import get_object_or_404
from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView
from accounts.permissions import IsVerifiedAccount
from accounts.request_limits import LimitedRequestAPIView
from accounts.throttles import ResearchUserThrottle

from research.models import ResearchJob
from research.serializers import (
    PersonalizedResearchRequestSerializer,
    ResearchReportSerializer,
)
from research.services.research_generation_service import (
    ResearchGenerationError,
    ResearchGenerationService,
)


class PersonalizedResearchView(LimitedRequestAPIView):
    permission_classes = [IsVerifiedAccount]
    throttle_classes = [ResearchUserThrottle]
    max_request_bytes = 65_536

    def post(self, request):
        serializer = PersonalizedResearchRequestSerializer(data=request.data)
        if not serializer.is_valid():
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        data = dict(serializer.validated_data)
        device_id = data.pop("device_id", "")
        try:
            job, report = ResearchGenerationService().generate_personalized(
                user=request.user,
                device_id=device_id,
                input_data=data,
            )
        except ResearchGenerationError as exc:
            return Response(
                {
                    "job_id": str(exc.job.id),
                    "status": exc.job.status,
                    "error": str(exc),
                },
                status=exc.http_status,
            )

        return Response(
            {
                "job_id": str(job.id),
                "status": job.status,
                "report": ResearchReportSerializer(report).data,
            },
            status=status.HTTP_201_CREATED,
        )


class ResearchJobStatusView(APIView):
    permission_classes = [IsVerifiedAccount]

    def get(self, request, job_id):
        job = get_object_or_404(ResearchJob, id=job_id, user=request.user)
        data = {
            "job_id": str(job.id),
            "status": job.status,
            "created_at": job.created_at,
            "started_at": job.started_at,
            "completed_at": job.completed_at,
        }
        if job.status == "FAILED":
            data["error"] = job.error_message
        if job.status == "COMPLETED":
            data["report_url"] = f"/api/v1/research/jobs/{job.id}/report/"
        return Response(data, status=status.HTTP_200_OK)


class ResearchJobReportView(APIView):
    permission_classes = [IsVerifiedAccount]

    def get(self, request, job_id):
        job = get_object_or_404(
            ResearchJob.objects.select_related("report"),
            id=job_id,
            user=request.user,
        )
        if job.status != "COMPLETED":
            return Response(
                {"job_id": str(job.id), "status": job.status},
                status=status.HTTP_409_CONFLICT,
            )
        return Response(
            ResearchReportSerializer(job.report).data,
            status=status.HTTP_200_OK,
        )
