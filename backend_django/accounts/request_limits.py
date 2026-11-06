from rest_framework import status
from rest_framework.exceptions import APIException
from rest_framework.views import APIView


class RequestTooLarge(APIException):
    status_code = status.HTTP_413_REQUEST_ENTITY_TOO_LARGE
    default_detail = "Request body exceeds the allowed size."
    default_code = "request_too_large"


class LimitedRequestAPIView(APIView):
    max_request_bytes = 16_384

    def initial(self, request, *args, **kwargs):
        try:
            content_length = int(request.META.get("CONTENT_LENGTH", "0"))
        except (TypeError, ValueError) as exc:
            raise RequestTooLarge() from exc
        if content_length > self.max_request_bytes:
            raise RequestTooLarge()
        return super().initial(request, *args, **kwargs)
