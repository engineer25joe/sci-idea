from django.contrib.auth import get_user_model
from rest_framework import serializers
from rest_framework.exceptions import AuthenticationFailed
from rest_framework_simplejwt.exceptions import InvalidToken, TokenError
from rest_framework_simplejwt.serializers import TokenRefreshSerializer
from rest_framework_simplejwt.settings import api_settings
from rest_framework_simplejwt.tokens import RefreshToken


User = get_user_model()


class VerifiedTokenRefreshSerializer(TokenRefreshSerializer):
    def validate(self, attrs):
        try:
            token = RefreshToken(attrs["refresh"])
            user_id = token[api_settings.USER_ID_CLAIM]
            user = User.objects.get(**{api_settings.USER_ID_FIELD: user_id})
            profile = user.account_profile
        except (TokenError, User.DoesNotExist, AttributeError, KeyError) as exc:
            raise InvalidToken("The refresh token is invalid or revoked.") from exc

        if not profile.can_use_protected_services:
            raise AuthenticationFailed("The account is unavailable.")
        return super().validate(attrs)
