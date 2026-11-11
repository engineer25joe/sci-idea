from django.contrib.auth import get_user_model
from django.db import IntegrityError, transaction
from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework_simplejwt.exceptions import TokenError
from rest_framework_simplejwt.token_blacklist.models import (
    BlacklistedToken,
    OutstandingToken,
)
from rest_framework_simplejwt.tokens import RefreshToken

from accounts.models import AccountActionToken, AccountProfile
from accounts.permissions import IsVerifiedAccount
from accounts.request_limits import LimitedRequestAPIView
from accounts.serializers import (
    AccountProfileSerializer,
    AccountProfileUpdateSerializer,
    EmailTokenSerializer,
    LogoutSerializer,
    LoginSerializer,
    PasswordResetConfirmSerializer,
    PasswordResetRequestSerializer,
    RegistrationSerializer,
)
from accounts.serializers_auth import VerifiedTokenRefreshSerializer
from accounts.services import (
    consume_account_action_token,
    send_account_action_email,
)
from accounts.throttles import AuthRequestThrottle, RefreshRequestThrottle


User = get_user_model()


class RegisterView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [AuthRequestThrottle]

    def post(self, request):
        serializer = RegistrationSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        data = serializer.validated_data

        try:
            with transaction.atomic():
                user = User.objects.create_user(
                    username=data["email"],
                    email=data["email"],
                    password=data["password"],
                    first_name=data.get("first_name", ""),
                    last_name=data.get("last_name", ""),
                )

                AccountProfile.objects.create(
                    user=user,
                    phone_number=data["phone_number"],
                )

        except IntegrityError:
            return Response(
                {
                    "error": (
                        "An account with this email or phone number "
                        "already exists."
                    )
                },
                status=status.HTTP_400_BAD_REQUEST,
            )

        try:
            send_account_action_email(
                user, AccountActionToken.EMAIL_VERIFICATION
            )
        except Exception:
            return Response(
                {
                    "error": (
                        "Account created; verification email could not "
                        "be sent. Request another code later."
                    )
                },
                status=status.HTTP_503_SERVICE_UNAVAILABLE,
            )

        return Response(
            {
                "detail": (
                    "Account created. Check your email for a "
                    "verification code."
                )
            },
            status=status.HTTP_201_CREATED,
        )


class VerifyEmailView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [AuthRequestThrottle]

    def post(self, request):
        serializer = EmailTokenSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        user = consume_account_action_token(
            serializer.validated_data["token"],
            AccountActionToken.EMAIL_VERIFICATION,
        )

        if user is None:
            return Response(
                {"error": "Verification code is invalid or expired."},
                status=status.HTTP_400_BAD_REQUEST,
            )

        profile = user.account_profile

        if profile.email_verified:
            return Response(
                {"error": "Verification code is invalid or expired."},
                status=status.HTTP_400_BAD_REQUEST,
            )

        profile.email_verified = True
        profile.save(update_fields=["email_verified", "updated_at"])

        return Response(
            {"detail": "Email verified. You can now sign in."}
        )


class ResendVerificationView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [AuthRequestThrottle]

    def post(self, request):
        serializer = PasswordResetRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        user = User.objects.filter(
            email__iexact=serializer.validated_data["email"]
        ).first()

        if user and user.is_active:
            profile, _ = AccountProfile.objects.get_or_create(user=user)

            if not profile.email_verified and profile.suspended_at is None:
                try:
                    send_account_action_email(
                        user, AccountActionToken.EMAIL_VERIFICATION
                    )
                except Exception:
                    pass

        return Response(
            {
                "detail": (
                    "If the account needs verification, a code has been sent."
                )
            }
        )


class LoginView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [AuthRequestThrottle]

    def post(self, request):
        serializer = LoginSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        identifier = serializer.validated_data["identifier"].strip()
        password = serializer.validated_data["password"]

        if "@" in identifier:
            user = User.objects.filter(
                email__iexact=identifier.lower()
            ).first()
        else:
            from accounts.serializers import normalize_phone_number

            try:
                phone_number = normalize_phone_number(identifier)
            except Exception:
                user = None
            else:
                profile = AccountProfile.objects.select_related(
                    "user"
                ).filter(phone_number=phone_number).first()

                user = profile.user if profile else None

        if user is None or not user.check_password(password):
            return Response(
                {"error": "Email/phone number or password is incorrect."},
                status=status.HTTP_401_UNAUTHORIZED,
            )

        profile, _ = AccountProfile.objects.get_or_create(user=user)

        if not profile.email_verified:
            return Response(
                {
                    "error": "Verify your email before signing in.",
                    "code": "EMAIL_UNVERIFIED",
                },
                status=status.HTTP_403_FORBIDDEN,
            )

        if not profile.can_use_protected_services:
            return Response(
                {"error": "This account is unavailable."},
                status=status.HTTP_403_FORBIDDEN,
            )

        refresh = RefreshToken.for_user(user)

        return Response(
            {
                "access": str(refresh.access_token),
                "refresh": str(refresh),
                "profile": AccountProfileSerializer(profile).data,
            }
        )


class LogoutView(LimitedRequestAPIView):
    permission_classes = [IsVerifiedAccount]

    def post(self, request):
        serializer = LogoutSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        try:
            token = RefreshToken(serializer.validated_data["refresh"])

            if str(token.get("user_id")) != str(request.user.pk):
                raise TokenError("Token does not belong to this account.")

            token.blacklist()

        except TokenError:
            return Response(
                {"error": "Refresh token is invalid or revoked."},
                status=status.HTTP_400_BAD_REQUEST,
            )

        return Response(status=status.HTTP_204_NO_CONTENT)


class AccountTokenRefreshView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [RefreshRequestThrottle]
    serializer_class = VerifiedTokenRefreshSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)

        return Response(
            serializer.validated_data,
            status=status.HTTP_200_OK,
        )


class PasswordResetRequestView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [AuthRequestThrottle]

    def post(self, request):
        serializer = PasswordResetRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        user = User.objects.filter(
            email__iexact=serializer.validated_data["email"]
        ).first()

        if user and user.is_active:
            profile, _ = AccountProfile.objects.get_or_create(user=user)

            if profile.can_use_protected_services:
                try:
                    send_account_action_email(
                        user, AccountActionToken.PASSWORD_RESET
                    )
                except Exception:
                    pass

        return Response(
            {
                "detail": (
                    "If an active verified account matches that email, "
                    "reset instructions have been sent."
                )
            }
        )


class PasswordResetConfirmView(LimitedRequestAPIView):
    permission_classes = [AllowAny]
    throttle_classes = [AuthRequestThrottle]

    def post(self, request):
        serializer = PasswordResetConfirmSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        with transaction.atomic():
            user = consume_account_action_token(
                serializer.validated_data["token"],
                AccountActionToken.PASSWORD_RESET,
            )

            if user is None or not user.is_active:
                return Response(
                    {"error": "Reset code is invalid or expired."},
                    status=status.HTTP_400_BAD_REQUEST,
                )

            user.set_password(serializer.validated_data["password"])
            user.save(update_fields=["password"])

            for outstanding in OutstandingToken.objects.filter(user=user):
                BlacklistedToken.objects.get_or_create(token=outstanding)

        return Response(
            {
                "detail": (
                    "Password has been reset. Sign in with the new password."
                )
            }
        )


class AccountProfileView(LimitedRequestAPIView):
    permission_classes = [IsVerifiedAccount]

    def get(self, request):
        profile, _ = AccountProfile.objects.get_or_create(user=request.user)
        return Response(AccountProfileSerializer(profile).data)

    def patch(self, request):
        serializer = AccountProfileUpdateSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        user = request.user

        for field in ("first_name", "last_name"):
            if field in serializer.validated_data:
                setattr(user, field, serializer.validated_data[field])

        user.save(
            update_fields=[
                field
                for field in ("first_name", "last_name")
                if field in serializer.validated_data
            ]
        )

        return Response(
            AccountProfileSerializer(user.account_profile).data
        )