from django.contrib import admin

from .models import (
    ResearchJob,
    ResearchReport,
    ResearchSource,
    ResearchRecommendation,
)


@admin.register(ResearchJob)
class ResearchJobAdmin(admin.ModelAdmin):
    list_display = (
        "id",
        "job_type",
        "status",
        "device_id",
        "created_at",
    )

    list_filter = (
        "job_type",
        "status",
        "created_at",
    )

    search_fields = (
        "device_id",
        "id",
    )

    readonly_fields = (
        "id",
        "created_at",
        "started_at",
        "completed_at",
    )


@admin.register(ResearchReport)
class ResearchReportAdmin(admin.ModelAdmin):
    list_display = (
        "title",
        "sector",
        "county",
        "review_status",
        "created_at",
    )

    list_filter = (
        "sector",
        "county",
        "review_status",
    )

    search_fields = (
        "title",
        "summary",
        "sector",
        "county",
    )

    readonly_fields = (
        "created_at",
        "updated_at",
    )


@admin.register(ResearchSource)
class ResearchSourceAdmin(admin.ModelAdmin):
    list_display = (
        "title",
        "publisher",
        "verification_status",
        "created_at",
    )

    list_filter = (
        "verification_status",
        "publisher",
    )

    search_fields = (
        "title",
        "publisher",
        "url",
    )


@admin.register(ResearchRecommendation)
class ResearchRecommendationAdmin(admin.ModelAdmin):
    list_display = (
        "title",
        "category",
        "estimated_startup_min_kes",
        "estimated_startup_max_kes",
        "confidence_score",
    )

    list_filter = (
        "category",
    )

    search_fields = (
        "title",
        "description",
        "category",
    )