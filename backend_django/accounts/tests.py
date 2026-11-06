import re
from django.contrib.auth import get_user_model
from django.core import mail
from django.test import TestCase, override_settings
from django.utils import timezone
from rest_framework.test import APIClient
from rest_framework_simplejwt.tokens import AccessToken, RefreshToken

from accounts.models import AccountProfile


User = get_user_model()
PASSWORD = "Sufficiently-Strong-Password-2026!"


@override_settings(EMAIL_BACKEND="django.core.mail.backends.locmem.EmailBackend")
class AccountSecurityTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        mail.outbox.clear()

    def create_account(self, email="person@example.com", verified=True):
        user = User.objects.create_user(
            username=email.lower(), email=email.lower(), password=PASSWORD
        )
        AccountProfile.objects.create(user=user, email_verified=verified)
        return user

    def authenticate(self, user):
        token = AccessToken.for_user(user)
        self.client.credentials(HTTP_AUTHORIZATION=f"Bearer {token}")
        return token

    def test_registration_hashes_password_and_verification_code_is_one_time(self):
        response = self.client.post(
            "/api/v1/auth/register/",
            {
                "email": "new.person@example.com",
                "password": PASSWORD,
                "password_confirm": PASSWORD,
                "first_name": "New",
            },
            format="json",
        )
        self.assertEqual(response.status_code, 201)
        user = User.objects.get(email="new.person@example.com")
        self.assertNotEqual(user.password, PASSWORD)
        self.assertTrue(user.check_password(PASSWORD))
        token = re.search(
            r"one-time code to verify your email address:\s*(\S+)",
            mail.outbox[-1].body,
        ).group(1)

        verify = self.client.post(
            "/api/v1/auth/verify-email/", {"token": token}, format="json"
        )
        reused = self.client.post(
            "/api/v1/auth/verify-email/", {"token": token}, format="json"
        )
        self.assertEqual(verify.status_code, 200)
        self.assertEqual(reused.status_code, 400)
        self.assertTrue(user.account_profile.email_verified)

    def test_unverified_account_cannot_login_or_access_research(self):
        user = self.create_account(verified=False)
        login = self.client.post(
            "/api/v1/auth/login/",
            {"email": user.email, "password": PASSWORD},
            format="json",
        )
        self.assertEqual(login.status_code, 403)

        self.authenticate(user)
        response = self.client.post(
            "/api/v1/research/personalised/", {}, format="json"
        )
        self.assertEqual(response.status_code, 403)

    def test_invalid_and_expired_access_tokens_are_rejected(self):
        user = self.create_account()
        job_id = "00000000-0000-0000-0000-000000000001"

        self.client.credentials(HTTP_AUTHORIZATION="Bearer invalid-token")
        invalid = self.client.get(f"/api/v1/research/jobs/{job_id}/")
        self.assertEqual(invalid.status_code, 401)

        expired = AccessToken.for_user(user)
        expired["exp"] = int(timezone.now().timestamp()) - 1
        self.client.credentials(HTTP_AUTHORIZATION=f"Bearer {expired}")
        expired_response = self.client.get(f"/api/v1/research/jobs/{job_id}/")
        self.assertEqual(expired_response.status_code, 401)

    def test_missing_authentication_cannot_use_a_known_job_uuid(self):
        job_id = "00000000-0000-0000-0000-000000000001"
        response = self.client.get(f"/api/v1/research/jobs/{job_id}/")
        report = self.client.get(f"/api/v1/research/jobs/{job_id}/report/")
        self.assertEqual(response.status_code, 401)
        self.assertEqual(report.status_code, 401)

    def test_device_id_header_does_not_authenticate_research_generation(self):
        response = self.client.post(
            "/api/v1/research/personalised/",
            {"device_id": "known-device", "budget_kes": 45000},
            format="json",
            HTTP_X_DEVICE_ID="known-device",
        )
        self.assertEqual(response.status_code, 401)

    def test_rotated_refresh_token_cannot_be_reused(self):
        user = self.create_account()
        original = str(RefreshToken.for_user(user))

        rotated = self.client.post(
            "/api/v1/auth/token/refresh/", {"refresh": original}, format="json"
        )
        self.assertEqual(rotated.status_code, 200)
        self.assertNotEqual(rotated.data["refresh"], original)
        reused = self.client.post(
            "/api/v1/auth/token/refresh/", {"refresh": original}, format="json"
        )
        self.assertEqual(reused.status_code, 401)

    def test_password_reset_token_is_one_time_and_revokes_refresh_tokens(self):
        user = self.create_account()
        old_refresh = str(RefreshToken.for_user(user))
        request = self.client.post(
            "/api/v1/auth/password-reset/",
            {"email": user.email},
            format="json",
        )
        self.assertEqual(request.status_code, 200)
        token = re.search(
            r"one-time code to reset your password:\s*(\S+)",
            mail.outbox[-1].body,
        ).group(1)
        new_password = "Another-Excellent-Password-2026!"
        payload = {
            "token": token,
            "password": new_password,
            "password_confirm": new_password,
        }
        changed = self.client.post(
            "/api/v1/auth/password-reset/confirm/", payload, format="json"
        )
        reused = self.client.post(
            "/api/v1/auth/password-reset/confirm/", payload, format="json"
        )
        refresh_after_reset = self.client.post(
            "/api/v1/auth/token/refresh/", {"refresh": old_refresh}, format="json"
        )
        user.refresh_from_db()
        self.assertEqual(changed.status_code, 200)
        self.assertEqual(reused.status_code, 400)
        self.assertEqual(refresh_after_reset.status_code, 401)
        self.assertTrue(user.check_password(new_password))

    def test_account_profile_requires_verified_authenticated_account(self):
        user = self.create_account()
        self.authenticate(user)
        response = self.client.get("/api/v1/auth/profile/")
        self.assertEqual(response.status_code, 200)
        updated = self.client.patch(
            "/api/v1/auth/profile/", {"first_name": "Changed"}, format="json"
        )
        self.assertEqual(updated.status_code, 200)
        user.refresh_from_db()
        self.assertEqual(user.first_name, "Changed")

    def test_suspended_account_cannot_use_existing_access_token(self):
        user = self.create_account()
        token = self.authenticate(user)
        user.is_active = False
        user.save(update_fields=["is_active"])
        response = self.client.get("/api/v1/auth/profile/")
        self.assertEqual(response.status_code, 401)
