import hashlib
import secrets
from datetime import timedelta

from django.conf import settings
from django.core.mail import send_mail
from django.db import transaction
from django.utils import timezone

from accounts.models import AccountActionToken


TOKEN_LIFETIMES = {
    AccountActionToken.EMAIL_VERIFICATION: timedelta(hours=24),
    AccountActionToken.PASSWORD_RESET: timedelta(minutes=30),
}


def _digest(raw_token):
    return hashlib.sha256(raw_token.encode("utf-8")).hexdigest()


def issue_account_action_token(user, purpose):
    raw_token = secrets.token_urlsafe(32)
    now = timezone.now()
    with transaction.atomic():
        AccountActionToken.objects.filter(
            user=user,
            purpose=purpose,
            consumed_at__isnull=True,
        ).delete()
        AccountActionToken.objects.create(
            user=user,
            purpose=purpose,
            token_digest=_digest(raw_token),
            expires_at=now + TOKEN_LIFETIMES[purpose],
        )
    return raw_token


def send_account_action_email(user, purpose):
    raw_token = issue_account_action_token(user, purpose)
    if purpose == AccountActionToken.EMAIL_VERIFICATION:
        subject = "Verify your SCI IDEA account"
        action = "verify your email address"
        lifetime = "24 hours"
    else:
        subject = "Reset your SCI IDEA password"
        action = "reset your password"
        lifetime = "30 minutes"
    message = (
        f"Use this one-time code to {action}:\n\n{raw_token}\n\n"
        f"The code expires in {lifetime}. If you did not request this, ignore this email."
    )
    send_mail(
        subject=subject,
        message=message,
        from_email=settings.DEFAULT_FROM_EMAIL,
        recipient_list=[user.email],
        fail_silently=False,
    )


@transaction.atomic
def consume_account_action_token(raw_token, purpose):
    token = (
        AccountActionToken.objects.select_for_update()
        .select_related("user")
        .filter(token_digest=_digest(raw_token), purpose=purpose)
        .first()
    )
    now = timezone.now()
    if (
        token is None
        or token.consumed_at is not None
        or token.expires_at <= now
    ):
        return None
    token.consumed_at = now
    token.save(update_fields=["consumed_at"])
    return token.user
