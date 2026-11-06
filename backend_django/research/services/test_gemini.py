from pathlib import Path
import sys

from dotenv import load_dotenv

# Load the backend .env file for this standalone test.
BACKEND_DIR = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(BACKEND_DIR))
load_dotenv(BACKEND_DIR / ".env")

from research.services.gemini_service import (
    GeminiService,
    GeminiServiceError,
)


def main():
    try:
        service = GeminiService()

        result = service.generate_json(
            prompt=(
                "Return a JSON object containing exactly these fields: "
                "status and message. Set status to success and message "
                "to SCI IDEA Gemini connection successful."
            ),
            system_instruction=(
                "You are a backend connectivity test. "
                "Return valid JSON only."
            ),
        )

        print("Gemini connection: SUCCESS")
        print("Response:", result)

    except GeminiServiceError as exc:
        print("Gemini connection: FAILED")
        print("Reason:", str(exc))


if __name__ == "__main__":
    main()
