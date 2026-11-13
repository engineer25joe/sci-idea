from django.contrib import admin, messages
from django.shortcuts import get_object_or_404, redirect
from django.urls import path, reverse

from accounts.models import AccountActionToken, AccountProfile


@admin.register(AccountProfile)
class AccountProfileAdmin(admin.ModelAdmin):
    change_form_template = (
        "admin/accounts/accountprofile/change_form.html"
    )

    list_display = (
        "user",
        "phone_number",
        "email_verified",
        "suspended_at",
        "created_at",
    )

    list_filter = ("email_verified", "suspended_at")

    search_fields = (
        "user__email",
        "user__username",
        "phone_number",
    )

    readonly_fields = ("created_at", "updated_at")

    def get_urls(self):
        urls = super().get_urls()

        custom_urls = [
            path(
                "<path:object_id>/manual-verify/",
                self.admin_site.admin_view(
                    self.manual_verify_view
                ),
                name="accounts_accountprofile_manual_verify",
            ),
        ]

        return custom_urls + urls

    def manual_verify_view(self, request, object_id):
        if request.method != "POST":
            return redirect(
                reverse(
                    "admin:accounts_accountprofile_change",
                    args=[object_id],
                )
            )

        profile = get_object_or_404(
            AccountProfile.objects.select_related("user"),
            pk=object_id,
        )

        if not self.has_change_permission(request, profile):
            messages.error(
                request,
                "You do not have permission to verify this account.",
            )
        elif not profile.user.is_active:
            messages.error(
                request,
                "Inactive accounts cannot be verified.",
            )
        elif profile.suspended_at is not None:
            messages.error(
                request,
                "Suspended accounts cannot be verified.",
            )
        elif profile.email_verified:
            messages.info(
                request,
                "This account is already verified.",
            )
        else:
            profile.email_verified = True
            profile.save(
                update_fields=["email_verified", "updated_at"]
            )

            messages.success(
                request,
                f"{profile.user.email} has been manually verified.",
            )

        return redirect(
            reverse(
                "admin:accounts_accountprofile_change",
                args=[object_id],
            )
        )

    def change_view(
        self, request, object_id, form_url="", extra_context=None
    ):
        extra_context = extra_context or {}

        extra_context["manual_verify_url"] = reverse(
            "admin:accounts_accountprofile_manual_verify",
            args=[object_id],
        )

        return super().change_view(
            request,
            object_id,
            form_url,
            extra_context=extra_context,
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

    list_filter = ("purpose", "consumed_at")

    search_fields = ("user__email",)

    readonly_fields = (
        "user",
        "purpose",
        "token_digest",
        "expires_at",
        "consumed_at",
        "created_at",
    )