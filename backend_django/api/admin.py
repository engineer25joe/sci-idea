from django.contrib import admin, messages
from django.utils import timezone

from .models import (
    BusinessIdea,
    Testimonial,
    UserActivityLog,
    Category,
    Skill,
    SkillCategory,
    UserSkillSelection,
)


@admin.register(BusinessIdea)
class BusinessIdeaAdmin(admin.ModelAdmin):
    list_display = (
        'title', 'category', 'capital_tier',
        'capital_min_kes', 'capital_max_kes',
        'rating', 'is_active'
    )
    list_filter = ('category', 'capital_tier', 'is_active')
    search_fields = ('title', 'summary', 'idea_id')


@admin.register(Testimonial)
class TestimonialAdmin(admin.ModelAdmin):
    list_display = (
        'name', 'business_name', 'county',
        'starting_capital_kes',
        'current_monthly_revenue_kes',
        'is_featured'
    )
    list_filter = ('county', 'is_featured')
    search_fields = ('name', 'business_name', 'quote')


@admin.register(UserActivityLog)
class UserActivityLogAdmin(admin.ModelAdmin):
    list_display = (
        'event_type', 'target_title', 'category',
        'device_id', 'created_at'
    )
    list_filter = ('event_type', 'category')
    search_fields = ('target_title', 'device_id', 'event_type')
    readonly_fields = (
        'device_id', 'event_id', 'event_type',
        'target_id', 'target_title', 'category',
        'metadata_json', 'timestamp_ms', 'created_at'
    )


@admin.register(Category)
class CategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'slug', 'color_hex')
    prepopulated_fields = {'slug': ('name',)}


@admin.register(SkillCategory)
class SkillCategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'code', 'display_order', 'is_active')
    list_editable = ('display_order', 'is_active')
    search_fields = ('name', 'code')
    ordering = ('display_order',)


@admin.action(description="Approve and publish selected skills")
def approve_skills(modeladmin, request, queryset):
    now = timezone.now()

    updated = queryset.filter(
        review_status="PENDING"
    ).update(
        review_status="APPROVED",
        is_active=True,
        reviewed_at=now,
        auto_publish_at=None,
    )

    modeladmin.message_user(
        request,
        f"{updated} skills approved and published.",
        messages.SUCCESS,
    )


@admin.action(description="Reject selected skills")
def reject_skills(modeladmin, request, queryset):
    now = timezone.now()

    updated = queryset.filter(
        review_status="PENDING"
    ).update(
        review_status="REJECTED",
        is_active=False,
        reviewed_at=now,
        auto_publish_at=None,
    )

    modeladmin.message_user(
        request,
        f"{updated} skills rejected.",
        messages.WARNING,
    )


@admin.register(Skill)
class SkillAdmin(admin.ModelAdmin):
    list_display = (
        'name',
        'category',
        'review_status',
        'is_active',
        'submitted_for_review_at',
        'auto_publish_at',
    )
    list_filter = (
        'category',
        'is_active',
        'review_status',
    )
    search_fields = ('name', 'description', 'id')
    ordering = ('category', 'display_order')

    actions = [
        approve_skills,
        reject_skills,
    ]

    readonly_fields = (
        'submitted_for_review_at',
        'reviewed_at',
        'auto_publish_at',
        'created_at',
        'updated_at',
    )


@admin.register(UserSkillSelection)
class UserSkillSelectionAdmin(admin.ModelAdmin):
    list_display = ('user', 'updated_at')
    search_fields = ('user__username', 'user__email')
    readonly_fields = ('updated_at',)
