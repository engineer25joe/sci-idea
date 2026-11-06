import uuid

from django.db import models
from django.conf import settings


class ResearchJob(models.Model):
    """
    Tracks personalized and autonomous research requests.
    """

    JOB_TYPES = [
        ("PERSONALIZED", "Personalized Research"),
        ("AUTONOMOUS", "Autonomous Research"),
    ]

    STATUS_CHOICES = [
        ("PENDING", "Pending"),
        ("PROCESSING", "Processing"),
        ("COMPLETED", "Completed"),
        ("FAILED", "Failed"),
        ("CANCELLED", "Cancelled"),
    ]

    id = models.UUIDField(
        primary_key=True,
        default=uuid.uuid4,
        editable=False,
    )

    job_type = models.CharField(
        max_length=20,
        choices=JOB_TYPES,
    )

    status = models.CharField(
        max_length=20,
        choices=STATUS_CHOICES,
        default="PENDING",
        db_index=True,
    )

    device_id = models.CharField(
        max_length=100,
        blank=True,
        db_index=True,
    )

    # Nullable for pre-account legacy rows. All newly created jobs set user;
    # historical rows remain preserved but are not visible to any account.
    user = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.PROTECT,
        null=True,
        blank=True,
        related_name="research_jobs",
        db_index=True,
    )

    input_data = models.JSONField(default=dict)

    error_message = models.TextField(blank=True)

    created_at = models.DateTimeField(auto_now_add=True)

    started_at = models.DateTimeField(null=True, blank=True)

    completed_at = models.DateTimeField(null=True, blank=True)

    class Meta:
        ordering = ["-created_at"]
        indexes = [
            models.Index(
                fields=["job_type", "status"],
                name="research_job_type_status_idx",
            ),
            models.Index(
                fields=["device_id", "-created_at"],
                name="research_device_created_idx",
            ),
        ]

    def __str__(self):
        return f"{self.job_type} - {self.status} - {self.id}"


class ResearchReport(models.Model):
    """
    Stores completed research reports.
    """

    REVIEW_STATUS = [
        ("PENDING", "Pending Review"),
        ("APPROVED", "Approved"),
        ("REJECTED", "Rejected"),
    ]

    job = models.OneToOneField(
        ResearchJob,
        on_delete=models.CASCADE,
        related_name="report",
    )

    title = models.CharField(max_length=250)

    summary = models.TextField()

    report_data = models.JSONField(default=dict)

    county = models.CharField(
        max_length=100,
        blank=True,
    )

    sector = models.CharField(
        max_length=100,
        blank=True,
        db_index=True,
    )

    review_status = models.CharField(
        max_length=20,
        choices=REVIEW_STATUS,
        default="PENDING",
        db_index=True,
    )

    reviewed_at = models.DateTimeField(
        null=True,
        blank=True,
    )

    published_at = models.DateTimeField(
        null=True,
        blank=True,
    )

    created_at = models.DateTimeField(auto_now_add=True)

    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        ordering = ["-created_at"]

    def __str__(self):
        return self.title


class ResearchSource(models.Model):
    """
    Records sources used to support research findings.
    """

    VERIFICATION_STATUS = [
        ("UNVERIFIED", "Unverified"),
        ("VERIFIED", "Verified"),
        ("UNAVAILABLE", "Unavailable"),
    ]

    report = models.ForeignKey(
        ResearchReport,
        on_delete=models.CASCADE,
        related_name="sources",
    )

    title = models.CharField(max_length=300)

    url = models.URLField(max_length=1000)

    publisher = models.CharField(
        max_length=200,
        blank=True,
    )

    description = models.TextField(blank=True)

    verification_status = models.CharField(
        max_length=20,
        choices=VERIFICATION_STATUS,
        default="UNVERIFIED",
    )

    accessed_at = models.DateTimeField(
        null=True,
        blank=True,
    )

    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title


class ResearchRecommendation(models.Model):
    """
    Stores individual business opportunities identified
    during research.
    """

    report = models.ForeignKey(
        ResearchReport,
        on_delete=models.CASCADE,
        related_name="recommendations",
    )

    title = models.CharField(max_length=250)

    category = models.CharField(
        max_length=100,
        blank=True,
    )

    description = models.TextField()

    estimated_startup_min_kes = models.PositiveBigIntegerField(
        null=True,
        blank=True,
    )

    estimated_startup_max_kes = models.PositiveBigIntegerField(
        null=True,
        blank=True,
    )

    confidence_score = models.PositiveSmallIntegerField(
        null=True,
        blank=True,
    )

    assumptions = models.JSONField(default=list)

    risks = models.JSONField(default=list)

    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ["-created_at"]

    def __str__(self):
        return self.title
