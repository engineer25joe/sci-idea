import json
import os
import time

import httpx
from google import genai
from google.genai import errors, types


MAX_ATTEMPTS = 4
INITIAL_BACKOFF_SECONDS = 1
MAX_BACKOFF_SECONDS = 8
RETRYABLE_STATUS_CODES = {429, 500, 502, 503, 504}
RETRYABLE_NETWORK_ERRORS = (
    httpx.TimeoutException,
    httpx.NetworkError,
)
RETRYABLE_EXCEPTIONS = (errors.APIError,) + RETRYABLE_NETWORK_ERRORS
CONFIGURATION_STATUS_CODES = {400, 401, 403, 404}


class GeminiServiceError(Exception):
    """Base error for Gemini configuration and request failures."""


class GeminiConfigurationError(GeminiServiceError):
    """The backend is not configured to make Gemini requests."""


class GeminiTemporaryError(GeminiServiceError):
    """Gemini is temporarily unavailable; the caller may retry later."""


class GeminiRetriesExhaustedError(GeminiTemporaryError):
    """All bounded retries for a temporary Gemini failure were used."""


class GeminiService:
    def __init__(self):
        # Keep credentials in backend environment only; never retain or log
        # the raw key on this service instance.
        api_key = os.getenv("GEMINI_API_KEY")
        self.model = os.getenv("GEMINI_MODEL", "gemini-3.8-flash").strip()

        if not api_key:
            raise GeminiConfigurationError(
                "Gemini API key is not configured in the backend environment."
            )
        if not self.model:
            raise GeminiConfigurationError(
                "GEMINI_MODEL is empty in the backend environment."
            )

        try:
            self.client = genai.Client(api_key=api_key)
        except Exception as exc:
            # Do not include provider exception text: it may contain request
            # details or credentials.
            raise GeminiConfigurationError(
                "Gemini client configuration failed. Check the backend API key and SDK configuration."
            ) from exc

    def generate_json(self, prompt, system_instruction=None):
        if not prompt or not prompt.strip():
            raise GeminiServiceError("Prompt cannot be empty.")

        for attempt in range(1, MAX_ATTEMPTS + 1):
            try:
                response = self.client.models.generate_content(
                    model=self.model,
                    contents=prompt,
                    config=types.GenerateContentConfig(
                        system_instruction=system_instruction,
                        response_mime_type="application/json",
                        temperature=0.3,
                    ),
                )

                if not response.text:
                    raise GeminiServiceError("Gemini returned an empty response.")

                result = json.loads(response.text)
                if not isinstance(result, dict):
                    raise GeminiServiceError("Unexpected Gemini response format.")
                return result

            except GeminiServiceError:
                raise
            except RETRYABLE_EXCEPTIONS as exc:
                status_code = getattr(exc, "code", None)
                if status_code is None:
                    status_code = getattr(exc, "status_code", None)

                retryable = (
                    isinstance(exc, RETRYABLE_NETWORK_ERRORS)
                    or status_code in RETRYABLE_STATUS_CODES
                )
                if not retryable:
                    # Surface a safe category and status, never raw provider
                    # diagnostics that could contain secrets.
                    if status_code in CONFIGURATION_STATUS_CODES:
                        raise GeminiConfigurationError(
                            f"Gemini configuration was rejected (HTTP {status_code}). Check the backend API key, model, and request configuration."
                        ) from exc
                    if status_code is not None:
                        raise GeminiServiceError(
                            f"Gemini rejected the request (HTTP {status_code}). Check the model, API key, and request configuration."
                        ) from exc
                    raise GeminiServiceError(
                        "Gemini request failed due to a non-retryable provider error."
                    ) from exc

                if attempt == MAX_ATTEMPTS:
                    reason = (
                        f"HTTP {status_code}"
                        if status_code is not None
                        else "network timeout or connection failure"
                    )
                    raise GeminiRetriesExhaustedError(
                        f"Gemini remained temporarily unavailable after {MAX_ATTEMPTS} attempts ({reason}). Try again later."
                    ) from exc

                delay = min(
                    INITIAL_BACKOFF_SECONDS * (2 ** (attempt - 1)),
                    MAX_BACKOFF_SECONDS,
                )
                time.sleep(delay)
            except Exception as exc:
                # JSON decoding and unexpected SDK errors are not transient.
                if isinstance(exc, json.JSONDecodeError):
                    raise GeminiServiceError(
                        "Gemini returned invalid JSON."
                    ) from exc
                raise GeminiServiceError(
                    "Gemini request failed due to an unexpected error."
                ) from exc
