from rest_framework import serializers


class PersonalizedResearchRequestSerializer(serializers.Serializer):
    device_id = serializers.CharField(
        max_length=100, trim_whitespace=True, required=False, default="", allow_blank=True
    )
    budget_kes = serializers.IntegerField(min_value=0)
    skills = serializers.ListField(
        child=serializers.CharField(max_length=100, trim_whitespace=True),
        allow_empty=False,
        max_length=30,
    )
    interests = serializers.ListField(
        child=serializers.CharField(max_length=100, trim_whitespace=True),
        allow_empty=False,
        max_length=30,
    )
    county = serializers.CharField(max_length=100, trim_whitespace=True)
    available_equipment = serializers.ListField(
        child=serializers.CharField(max_length=100, trim_whitespace=True),
        allow_empty=True,
        max_length=50,
    )
    available_time = serializers.CharField(max_length=200, trim_whitespace=True)
    preferred_business_category = serializers.CharField(
        max_length=100,
        trim_whitespace=True,
        allow_blank=True,
        required=False,
        default="",
    )

    def validate_device_id(self, value):
        return value

    def validate_county(self, value):
        if not value:
            raise serializers.ValidationError("This field may not be blank.")
        return value

    def validate_available_time(self, value):
        if not value:
            raise serializers.ValidationError("This field may not be blank.")
        return value

    def validate_skills(self, value):
        if any(not item for item in value):
            raise serializers.ValidationError("Skills cannot contain blank items.")
        return value

    def validate_interests(self, value):
        if any(not item for item in value):
            raise serializers.ValidationError("Interests cannot contain blank items.")
        return value

    def validate_available_equipment(self, value):
        if any(not item for item in value):
            raise serializers.ValidationError("Equipment cannot contain blank items.")
        return value


class ResearchRecommendationSerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    title = serializers.CharField()
    category = serializers.CharField()
    description = serializers.CharField()
    estimated_startup_min_kes = serializers.IntegerField(allow_null=True)
    estimated_startup_max_kes = serializers.IntegerField(allow_null=True)
    confidence_score = serializers.IntegerField(allow_null=True)
    assumptions = serializers.ListField(child=serializers.CharField())
    risks = serializers.ListField(child=serializers.CharField())


class ResearchSourceSerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    title = serializers.CharField()
    url = serializers.URLField()
    publisher = serializers.CharField(allow_blank=True)
    description = serializers.CharField(allow_blank=True)
    verification_status = serializers.CharField()


class ResearchReportSerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    job_id = serializers.UUIDField(read_only=True)
    title = serializers.CharField()
    summary = serializers.CharField()
    county = serializers.CharField()
    sector = serializers.CharField()
    report_data = serializers.JSONField()
    created_at = serializers.DateTimeField()
    sources = ResearchSourceSerializer(many=True, read_only=True)
    recommendations = ResearchRecommendationSerializer(many=True, read_only=True)
