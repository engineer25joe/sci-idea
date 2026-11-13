from django.contrib import admin, messages
from django.utils import timezone

from accounts.models import AccountActionToken, AccountProfile


@admin.register(AccountProfile)
class AccountProfileAdmin(admin.ModelAdmin):

    list_display = (
        "user",
        "phone_number",
        "email_verified",
        "suspended_at",
        "created_at",
    )

    list_filter = (
        "email_verified",
        "suspended_at",
    )

    search_fields = (
        "user__email",
        "user__username",
        "phone_number",
    )

    readonly_fields = (
        "created_at",
        "updated_at",
    )

    actions = (
        "manually_verify_accounts",
        "manually_unverify_accounts",
    )

    @admin.action(description="Verify selected accounts")
    def manually_verify_accounts(self, request, queryset):

        verified_count = 0

        for profile in queryset.select_related("user"):
            if not profile.user.is_active:
                continue

            if profile.suspended_at is not None:
                continue

            if not profile.email_verified:
                profile.email_verified = True
                profile.save(
                    update_fields=["email_verified", "updated_at"]
                )
                verified_count += 1

        self.message_user(
            request,
            f"{verified_count} account(s) successfully verified.",
            messages.SUCCESS,
        )

    @admin.action(description="Remove verification from selected accounts")
    def manually_unverify_accounts(self, request, queryset):

        updated_count = queryset.update(email_verified=False)

        self.message_user(
            request,
            f"Verification removed from {updated_count} account(s).",
            messages.WARNING,
        )


@admin.register(AccountActionToken)
class AccountActionTokenAdmin(admin.ModelAdmin):

    list_display = (
        "user",
        "purpose",
        "expires_at",
        "consumed_at",
        "created_at",
    )

    list_filter = (
        "purpose",
        "consumed_at",
    )

    search_fields = (
        "user__email",
    )

    readonly_fields = (
        "user",
        "purpose",
        "token_digest",
        "expires_at",
        "consumed_at",
        "created_at",
    )