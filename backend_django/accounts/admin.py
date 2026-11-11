from django.contrib import admin

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
    list_filter = ("email_verified",)
    search_fields = (
        "user__email",
        "user__username",
        "phone_number",
    )
    readonly_fields = ("created_at", "updated_at")


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