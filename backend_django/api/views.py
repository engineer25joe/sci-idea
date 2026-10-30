import json

from django.shortcuts import render
from django.contrib.admin.views.decorators import staff_member_required
from django.db import transaction
from django.db.models import Count
from django.views.decorators.http import require_GET

from rest_framework import generics, status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAdminUser
from rest_framework.throttling import AnonRateThrottle

from .models import BusinessIdea, Testimonial, UserActivityLog
from .serializers import (
    BusinessIdeaSerializer,
    TestimonialSerializer,
    EventBatchRequestSerializer,
)


# BUSINESS IDEAS API
class BusinessIdeaListCreateView(generics.ListCreateAPIView):
    serializer_class = BusinessIdeaSerializer

    pagination_class = None

    def get_queryset(self):
        if self.request.method == "GET":
            return BusinessIdea.objects.filter(
                is_active=True
            ).order_by("id")

        return BusinessIdea.objects.all().order_by("id")

    def get_permissions(self):
        if self.request.method == "GET":
            return [AllowAny()]

        return [IsAdminUser()]


# TESTIMONIALS API
class TestimonialListCreateView(generics.ListCreateAPIView):
    serializer_class = TestimonialSerializer

    pagination_class = None
    
    def get_queryset(self):
        if self.request.method == "GET":
            return Testimonial.objects.filter(
                is_featured=True
            ).order_by("-id")

        return Testimonial.objects.all().order_by("-id")

    def get_permissions(self):
        if self.request.method == "GET":
            return [AllowAny()]

        return [IsAdminUser()]


# ANONYMOUS TRACKING RATE LIMIT
class AnonymousTrackingThrottle(AnonRateThrottle):
    rate = "60/min"


# TRACKING EVENTS API
class TrackingEventsBatchView(APIView):
    permission_classes = [AllowAny]
    throttle_classes = [AnonymousTrackingThrottle]

    def post(self, request):
        serializer = EventBatchRequestSerializer(data=request.data)

        if not serializer.is_valid():
            return Response(
                serializer.errors,
                status=status.HTTP_400_BAD_REQUEST
            )

        device_id = serializer.validated_data["device_id"]
        events_data = serializer.validated_data["events"]

        with transaction.atomic():
            UserActivityLog.objects.bulk_create(
                [
                    UserActivityLog(
                        device_id=device_id,
                        event_id=item["event_id"],
                        event_type=item["event_type"],
                        target_id=item.get("target_id"),
                        target_title=item.get("target_title"),
                        category=item.get("category"),
                        metadata_json=json.dumps(
                            item.get("metadata", {})
                        ),
                        timestamp_ms=item["timestamp_ms"],
                    )
                    for item in events_data
                ],
                ignore_conflicts=True
            )

        return Response(
            {
                "status": "success",
                "synced_count": len(events_data),
            },
            status=status.HTTP_200_OK
        )


# ADMIN DASHBOARD
@staff_member_required
@require_GET
def web_admin_dashboard(request):
    total_events = UserActivityLog.objects.count()

    recent_events = UserActivityLog.objects.all().order_by("-id")[:40]

    total_ideas = BusinessIdea.objects.count()
    total_testimonials = Testimonial.objects.count()

    top_viewed_ideas = (
        UserActivityLog.objects.filter(event_type="VIEW_IDEA")
        .values("target_title")
        .annotate(views=Count("id"))
        .order_by("-views")[:6]
    )

    top_event_types = (
        UserActivityLog.objects.values("event_type")
        .annotate(count=Count("id"))
        .order_by("-count")
    )

    ideas_list = BusinessIdea.objects.all().order_by("-id")[:20]
    testimonials_list = Testimonial.objects.all().order_by("-id")[:10]

    context = {
        "total_events": total_events,
        "recent_events": recent_events,
        "total_ideas": total_ideas,
        "total_testimonials": total_testimonials,
        "top_viewed_ideas": top_viewed_ideas,
        "top_event_types": top_event_types,
        "ideas_list": ideas_list,
        "testimonials_list": testimonials_list,
    }

    return render(request, "admin_dashboard.html", context)