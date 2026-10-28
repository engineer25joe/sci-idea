from django.shortcuts import render
from django.http import JsonResponse
from django.db.models import Count
from rest_framework import generics, status
from rest_framework.views import APIView
from rest_framework.response import Response
from .models import BusinessIdea, Testimonial, UserActivityLog
from .serializers import (
    BusinessIdeaSerializer,
    TestimonialSerializer,
    EventBatchRequestSerializer,
    UserActivityLogSerializer
)
import json

class BusinessIdeaListCreateView(generics.ListCreateAPIView):
    queryset = BusinessIdea.objects.filter(is_active=True).order_by('id')
    serializer_class = BusinessIdeaSerializer

class TestimonialListCreateView(generics.ListCreateAPIView):
    queryset = Testimonial.objects.filter(is_featured=True).order_by('-id')
    serializer_class = TestimonialSerializer

class TrackingEventsBatchView(APIView):
    def post(self, request):
        serializer = EventBatchRequestSerializer(data=request.data)
        if not serializer.is_valid():
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        device_id = serializer.validated_data['device_id']
        events_data = serializer.validated_data['events']
        created_count = 0

        for item in events_data:
            meta = item.get('metadata', {})
            UserActivityLog.objects.create(
                device_id=device_id,
                event_id=item.get('event_id'),
                event_type=item.get('event_type'),
                target_id=item.get('target_id'),
                target_title=item.get('target_title'),
                category=item.get('category'),
                metadata_json=json.dumps(meta),
                timestamp_ms=item.get('timestamp_ms', 0)
            )
            created_count += 1

        return Response({
            "status": "success",
            "synced_count": created_count
        }, status=status.HTTP_201_CREATED)

def web_admin_dashboard(request):
    """
    Interactive Web Dashboard for remote management & live tracking of user behavior
    """
    total_events = UserActivityLog.objects.count()
    recent_events = UserActivityLog.objects.all().order_by('-id')[:40]
    total_ideas = BusinessIdea.objects.count()
    total_testimonials = Testimonial.objects.count()

    top_viewed_ideas = (
        UserActivityLog.objects.filter(event_type="VIEW_IDEA")
        .values('target_title')
        .annotate(views=Count('id'))
        .order_by('-views')[:6]
    )

    top_event_types = (
        UserActivityLog.objects.values('event_type')
        .annotate(count=Count('id'))
        .order_by('-count')
    )

    ideas_list = BusinessIdea.objects.all().order_by('-id')[:20]
    testimonials_list = Testimonial.objects.all().order_by('-id')[:10]

    context = {
        'total_events': total_events,
        'recent_events': recent_events,
        'total_ideas': total_ideas,
        'total_testimonials': total_testimonials,
        'top_viewed_ideas': top_viewed_ideas,
        'top_event_types': top_event_types,
        'ideas_list': ideas_list,
        'testimonials_list': testimonials_list
    }
    return render(request, 'admin_dashboard.html', context)
