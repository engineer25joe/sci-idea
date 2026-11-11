from django.conf import settings
from django.db import models


class AccountProfile(models.Model):
    user = models.OneToOneField(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
        related_name="account_profile",
    )

    # Nullable for compatibility with accounts created before phone registration.
    phone_number = models.CharField(
        max_length=16,
        unique=True,
        null=True,
        blank=True,
        db_index=True,
    )

    email_verified = models.BooleanField(default=False, db_index=True)
    suspended_at = models.DateTimeField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    @property
    def can_use_protected_services(self):
        return (
            self.user.is_active
            and self.email_verified
            and self.suspended_at is None
        )

    def __str__(self):
        return f"Account profile for user {self.user_id}"


class AccountActionToken(models.Model):
    EMAIL_VERIFICATION = "EMAIL_VERIFICATION"
    PASSWORD_RESET = "PASSWORD_RESET"

    PURPOSE_CHOICES = [
        (EMAIL_VERIFICATION, "Email verification"),
        (PASSWORD_RESET, "Password reset"),
    ]

    user = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
        related_name="account_action_tokens",
    )
    purpose = models.CharField(max_length=24, choices=PURPOSE_CHOICES)
    token_digest = models.CharField(max_length=64, unique=True)
    expires_at = models.DateTimeField(db_index=True)
    consumed_at = models.DateTimeField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        indexes = [
            models.Index(fields=["user", "purpose", "created_at"])
        ]

    def __str__(self):
        return f"{self.purpose} token for user {self.user_id}"