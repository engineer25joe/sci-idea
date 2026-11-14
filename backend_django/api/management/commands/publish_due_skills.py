from django.core.management.base import BaseCommand
from django.db import transaction
from django.utils import timezone

from api.models import Skill


class Command(BaseCommand):
    help = "Automatically publish skills pending approval for at least 24 hours."

    def handle(self, *args, **options):
        now = timezone.now()

        with transaction.atomic():
            due_skills = Skill.objects.filter(
                review_status="PENDING",
                is_active=False,
                auto_publish_at__isnull=False,
                auto_publish_at__lte=now,
            )

            count = due_skills.update(
                review_status="APPROVED",
                is_active=True,
                reviewed_at=now,
                auto_publish_at=None,
            )

        self.stdout.write(
            self.style.SUCCESS(
                f"Successfully auto-published {count} skill(s)."
            )
        )