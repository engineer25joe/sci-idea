from django.urls import path

from research.views import (
    PersonalizedResearchView,
    ResearchJobReportView,
    ResearchJobStatusView,
)


urlpatterns = [
    path(
        "personalised/",
        PersonalizedResearchView.as_view(),
        name="personalized-research",
    ),
    path(
        "jobs/<uuid:job_id>/",
        ResearchJobStatusView.as_view(),
        name="research-job-status",
    ),
    path(
        "jobs/<uuid:job_id>/report/",
        ResearchJobReportView.as_view(),
        name="research-job-report",
    ),
]
