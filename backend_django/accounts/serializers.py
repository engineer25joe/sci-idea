import re

from django.contrib.auth import get_user_model
from django.contrib.auth.password_validation import validate_password
from django.core.exceptions import ValidationError as DjangoValidationError
from rest_framework import serializers

from accounts.models import AccountProfile


User = get_user_model()


def normalize_phone_number(value):
    """
    Normalize Kenyan local numbers and international numbers to E.164.
    Examples:
        0712345678 -> +254712345678
        0112345678 -> +254112345678
        254712345678 -> +254712345678
        +254712345678 -> +254712345678
    """
    value = re.sub(r"[\s\-()]", "", value.strip())

    if value.startswith("00"):
        value = "+" + value[2:]

    if value.startswith("0") and len(value) == 10:
        value = "+254" + value[1:]
    elif value.startswith("254") and not value.startswith("+"):
        value = "+" + value

    if not re.fullmatch(r"\+[1-9]\d{7,14}", value):
        raise serializers.ValidationError(
            "Enter a valid phone number with its country code."
        )

    return value


class RegistrationSerializer(serializers.Serializer):
    email = serializers.EmailField(max_length=150)
    phone_number = serializers.CharField(max_length=30)
    password = serializers.CharField(
        write_only=True, trim_whitespace=False, max_length=128
    )
    password_confirm = serializers.CharField(
        write_only=True, trim_whitespace=False, max_length=128
    )
    first_name = serializers.CharField(
        max_length=150, required=False, allow_blank=True
    )
    last_name = serializers.CharField(
        max_length=150, required=False, allow_blank=True
    )

    def validate_email(self, value):
        value = value.strip().lower()

        if User.objects.filter(email__iexact=value).exists():
            raise serializers.ValidationError(
                "An account with this email already exists."
            )

        return value

    def validate_phone_number(self, value):
        value = normalize_phone_number(value)

        if AccountProfile.objects.filter(phone_number=value).exists():
            raise serializers.ValidationError(
                "An account with this phone number already exists."
            )

        return value

    def validate(self, attrs):
        if attrs["password"] != attrs["password_confirm"]:
            raise serializers.ValidationError(
                {"password_confirm": "Passwords do not match."}
            )

        candidate = User(
            username=attrs["email"],
            email=attrs["email"],
            first_name=attrs.get("first_name", ""),
            last_name=attrs.get("last_name", ""),
        )

        try:
            validate_password(attrs["password"], user=candidate)
        except DjangoValidationError as exc:
            raise serializers.ValidationError(
                {"password": list(exc.messages)}
            ) from exc

        return attrs


class LoginSerializer(serializers.Serializer):
    identifier = serializers.CharField(max_length=150)
    password = serializers.CharField(
        write_only=True, trim_whitespace=False, max_length=128
    )


class EmailTokenSerializer(serializers.Serializer):
    token = serializers.CharField(
        min_length=32, max_length=128, trim_whitespace=True
    )


class PasswordResetRequestSerializer(serializers.Serializer):
    email = serializers.EmailField(max_length=150)


class PasswordResetConfirmSerializer(EmailTokenSerializer):
    password = serializers.CharField(
        write_only=True, trim_whitespace=False, max_length=128
    )
    password_confirm = serializers.CharField(
        write_only=True, trim_whitespace=False, max_length=128
    )

    def validate(self, attrs):
        if attrs["password"] != attrs["password_confirm"]:
            raise serializers.ValidationError(
                {"password_confirm": "Passwords do not match."}
            )

        try:
            validate_password(attrs["password"])
        except DjangoValidationError as exc:
            raise serializers.ValidationError(
                {"password": list(exc.messages)}
            ) from exc

        return attrs


class LogoutSerializer(serializers.Serializer):
    refresh = serializers.CharField(
        min_length=32, max_length=4096, write_only=True
    )


class AccountProfileSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(source="user.email", read_only=True)
    first_name = serializers.CharField(
        source="user.first_name", read_only=True
    )
    last_name = serializers.CharField(
        source="user.last_name", read_only=True
    )
    email_verified = serializers.BooleanField(read_only=True)

    class Meta:
        model = AccountProfile
        fields = [
            "email",
            "phone_number",
            "first_name",
            "last_name",
            "email_verified",
            "created_at",
        ]
        read_only_fields = fields


class AccountProfileUpdateSerializer(serializers.Serializer):
    first_name = serializers.CharField(
        max_length=150, required=False, allow_blank=True
    )
    last_name = serializers.CharField(
        max_length=150, required=False, allow_blank=True
    )

    def validate(self, attrs):
        if not attrs:
            raise serializers.ValidationError(
                "Provide at least one profile field."
            )

        return attrs