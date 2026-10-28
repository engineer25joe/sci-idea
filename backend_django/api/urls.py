from django.urls import path
from .views import (
    BusinessIdeaListCreateView,
    TestimonialListCreateView,
    TrackingEventsBatchView,
    web_admin_dashboard
)

urlpatterns = [
    path('ideas/', BusinessIdeaListCreateView.as_view(), name='api_ideas_list'),
    path('testimonials/', TestimonialListCreateView.as_view(), name='api_testimonials_list'),
    path('tracking/events/', TrackingEventsBatchView.as_view(), name='api_tracking_events'),
    path('dashboard/', web_admin_dashboard, name='dashboard_view'),
]
