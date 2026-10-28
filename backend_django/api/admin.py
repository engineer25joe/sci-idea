from django.contrib import admin
from .models import BusinessIdea, Testimonial, UserActivityLog, Category

@admin.register(BusinessIdea)
class BusinessIdeaAdmin(admin.ModelAdmin):
    list_display = ('title', 'category', 'capital_tier', 'capital_min_kes', 'capital_max_kes', 'rating', 'is_active')
    list_filter = ('category', 'capital_tier', 'is_active')
    search_fields = ('title', 'summary', 'idea_id')

@admin.register(Testimonial)
class TestimonialAdmin(admin.ModelAdmin):
    list_display = ('name', 'business_name', 'county', 'starting_capital_kes', 'current_monthly_revenue_kes', 'is_featured')
    list_filter = ('county', 'is_featured')
    search_fields = ('name', 'business_name', 'quote')

@admin.register(UserActivityLog)
class UserActivityLogAdmin(admin.ModelAdmin):
    list_display = ('event_type', 'target_title', 'category', 'device_id', 'created_at')
    list_filter = ('event_type', 'category')
    search_fields = ('target_title', 'device_id', 'event_type')
    readonly_fields = ('device_id', 'event_id', 'event_type', 'target_id', 'target_title', 'category', 'metadata_json', 'timestamp_ms', 'created_at')

@admin.register(Category)
class CategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'slug', 'color_hex')
    prepopulated_fields = {'slug': ('name',)}
