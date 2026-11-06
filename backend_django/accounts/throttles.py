from rest_framework.throttling import AnonRateThrottle, UserRateThrottle


class AuthRequestThrottle(AnonRateThrottle):
    rate = "10/hour"


class RefreshRequestThrottle(AnonRateThrottle):
    rate = "30/hour"


class ResearchUserThrottle(UserRateThrottle):
    rate = "5/hour"
