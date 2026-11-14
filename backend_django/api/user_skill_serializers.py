from rest_framework import serializers
from .models import Skill, UserSkillSelection


class UserSkillSelectionSerializer(serializers.ModelSerializer):
    skill_ids = serializers.ListField(
        child=serializers.CharField(max_length=100),
        allow_empty=True,
        max_length=100
    )

    custom_skills = serializers.ListField(
        child=serializers.CharField(max_length=150, trim_whitespace=True),
        allow_empty=True,
        max_length=50,
        required=False
    )

    class Meta:
        model = UserSkillSelection
        fields = ['skill_ids', 'custom_skills', 'updated_at']
        read_only_fields = ['updated_at']

    def validate_skill_ids(self, value):
        if len(value) != len(set(value)):
            raise serializers.ValidationError(
                "Duplicate skill IDs are not allowed."
            )

        valid_ids = set(
            Skill.objects.filter(
                id__in=value,
                is_active=True,
                category__is_active=True
            ).values_list('id', flat=True)
        )

        invalid_ids = set(value) - valid_ids

        if invalid_ids:
            raise serializers.ValidationError(
                f"Invalid or inactive skill IDs: {', '.join(invalid_ids)}"
            )

        return value

    def validate_custom_skills(self, value):
        cleaned = [item.strip() for item in value if item.strip()]

        if len(cleaned) != len(set(item.lower() for item in cleaned)):
            raise serializers.ValidationError(
                "Duplicate custom skills are not allowed."
            )

        return cleaned