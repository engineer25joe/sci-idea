from rest_framework.permissions import IsAuthenticated


class IsVerifiedAccount(IsAuthenticated):
    message = "An active, verified account is required."

    def has_permission(self, request, view):
        if not super().has_permission(request, view):
            return False
        user = request.user
        try:
            profile = user.account_profile
        except Exception:
            return False
        return profile.can_use_protected_services
