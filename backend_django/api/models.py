from django.db import models
from django.conf import settings
import json

class Category(models.Model):
    name = models.CharField(max_length=100, unique=True)
    slug = models.SlugField(max_length=100, unique=True)
    description = models.TextField(blank=True)
    color_hex = models.CharField(max_length=20, default="#0B6638")

    class Meta:
        verbose_name_plural = "Categories"

    def __str__(self):
        return self.name

class BusinessIdea(models.Model):
    idea_id = models.CharField(max_length=100, unique=True, help_text="Unique slug ID e.g. urban_farming")
    title = models.CharField(max_length=200)
    category = models.CharField(max_length=100, default="Agriculture")
    summary = models.TextField()
    capital_min_kes = models.IntegerField(default=10000)
    capital_max_kes = models.IntegerField(default=50000)
    capital_tier = models.CharField(max_length=50, default="Low Capital")
    estimated_monthly_profit_min = models.IntegerField(default=15000)
    estimated_monthly_profit_max = models.IntegerField(default=60000)
    rating = models.FloatField(default=4.5)
    review_count = models.IntegerField(default=120)

    # JSON formatted fields for accordion details
    startup_requirements_json = models.TextField(default="[]", help_text="JSON list of requirements")
    potential_customers_json = models.TextField(default="[]", help_text="JSON list of customer segments")
    marketing_strategies_json = models.TextField(default="[]", help_text="JSON list of marketing strategies")
    next_steps_json = models.TextField(default="[]", help_text="JSON list of action steps")

    is_active = models.BooleanField(default=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.title

    @property
    def startup_requirements(self):
        try:
            return json.loads(self.startup_requirements_json)
        except Exception:
            return []

    @property
    def potential_customers(self):
        try:
            return json.loads(self.potential_customers_json)
        except Exception:
            return []

    @property
    def marketing_strategies(self):
        try:
            return json.loads(self.marketing_strategies_json)
        except Exception:
            return []

    @property
    def next_steps(self):
        try:
            return json.loads(self.next_steps_json)
        except Exception:
            return []

class Testimonial(models.Model):
    name = models.CharField(max_length=150)
    business_name = models.CharField(max_length=200)
    county = models.CharField(max_length=100, default="Nairobi")
    quote = models.TextField()
    starting_capital_kes = models.IntegerField(default=5000)
    current_monthly_revenue_kes = models.IntegerField(default=120000)
    advice = models.TextField()
    is_featured = models.BooleanField(default=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.name} - {self.business_name}"

class UserActivityLog(models.Model):
    device_id = models.CharField(max_length=100, db_index=True)
    event_id = models.CharField(max_length=100, blank=True, null=True)
    event_type = models.CharField(max_length=60, db_index=True) # VIEW_IDEA, SEARCH, SAVE_IDEA, etc.
    target_id = models.CharField(max_length=100, blank=True, null=True)
    target_title = models.CharField(max_length=200, blank=True, null=True)
    category = models.CharField(max_length=100, blank=True, null=True)
    metadata_json = models.TextField(default="{}")
    timestamp_ms = models.BigIntegerField(default=0)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-timestamp_ms']
        constraints = [
            models.UniqueConstraint(
                fields=['device_id', 'event_id'],
                name='unique_device_tracking_event'
            )
        ]
    def __str__(self):
        return f"[{self.event_type}] {self.target_title or self.target_id or 'General'} by {self.device_id}"

class SkillCategory(models.Model):
    code = models.CharField(max_length=60, unique=True)
    name = models.CharField(max_length=100)
    description = models.TextField(blank=True)
    icon_name = models.CharField(max_length=60, blank=True)
    image_url = models.URLField(blank=True)
    display_order = models.PositiveIntegerField(default=0)
    is_active = models.BooleanField(default=True)

    class Meta:
        ordering = ['display_order', 'name']
        verbose_name_plural = 'Skill categories'

    def __str__(self):
        return self.name


class Skill(models.Model):
    REVIEW_STATUS = [
        ("PENDING", "Pending Approval"),
        ("APPROVED", "Approved"),
        ("REJECTED", "Rejected"),
    ]

    id = models.CharField(max_length=100, primary_key=True)
    name = models.CharField(max_length=150)
    category = models.ForeignKey(
        SkillCategory,
        on_delete=models.PROTECT,
        related_name='skills'
    )
    description = models.TextField(blank=True)
    image_url = models.URLField(blank=True)
    display_order = models.PositiveIntegerField(default=0)
    is_active = models.BooleanField(default=True)

    review_status = models.CharField(
        max_length=20,
        choices=REVIEW_STATUS,
        default="APPROVED",
        db_index=True,
    )
    submitted_for_review_at = models.DateTimeField(
        null=True,
        blank=True,
    )
    reviewed_at = models.DateTimeField(
        null=True,
        blank=True,
    )
    auto_publish_at = models.DateTimeField(
        null=True,
        blank=True,
        db_index=True,
    )

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        ordering = ['display_order', 'name']

    def __str__(self):
        return self.name

    research_sources = models.JSONField(default=list, blank=True)


class UserSkillSelection(models.Model):
    user = models.OneToOneField(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
        related_name='skill_selection'
    )
    skill_ids = models.JSONField(default=list, blank=True)
    custom_skills = models.JSONField(default=list, blank=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"Skills selected by user {self.user_id}"
