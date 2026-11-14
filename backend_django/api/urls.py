from django.urls import include, path
from .user_skill_views import UserSkillSelectionView
from .skill_views import SkillListView, SkillCategoryListView
from .views import (
    BusinessIdeaListCreateView,
    TestimonialListCreateView,
    TrackingEventsBatchView,
    web_admin_dashboard
)
from .skill_generation_views import SkillGenerationView
from .skill_generation_views import AutomatedSkillPublishingView
urlpatterns = [
    path('auth/', include('accounts.urls')),
    path('ideas/', BusinessIdeaListCreateView.as_view(), name='api_ideas_list'),
    path('testimonials/', TestimonialListCreateView.as_view(), name='api_testimonials_list'),
    path('tracking/events/', TrackingEventsBatchView.as_view(), name='api_tracking_events'),
    path('research/', include('research.urls')),
    path('dashboard/', web_admin_dashboard, name='dashboard_view'),
    path('skills/', SkillListView.as_view(), name='api_skills'),
    path('skills/categories/', SkillCategoryListView.as_view(), name='api_skill_categories'),
    path(
        'skills/my-selection/',
        UserSkillSelectionView.as_view(),
        name='my_skill_selection'
    ),
    path('skills/generate/', SkillGenerationView.as_view(), name='api_skill_generate'),
    path(
        'internal/publish-due-skills/',
        AutomatedSkillPublishingView.as_view(),
        name='automated_skill_publishing',
    ),
]
