from rest_framework import serializers
from .models import Skill, SkillCategory


class SkillSerializer(serializers.ModelSerializer):
    category = serializers.CharField(source='category.code', read_only=True)

    class Meta:
        model = Skill
        fields = ['id', 'name', 'category', 'description', 'image_url']


class SkillCategorySerializer(serializers.ModelSerializer):
    skills = SkillSerializer(many=True, read_only=True)

    class Meta:
        model = SkillCategory
        fields = [
            'code', 'name', 'description',
            'icon_name', 'image_url', 'skills'
        ]
