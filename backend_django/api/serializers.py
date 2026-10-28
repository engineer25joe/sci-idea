from rest_framework import serializers
from .models import BusinessIdea, Testimonial, UserActivityLog
import json

class BusinessIdeaSerializer(serializers.ModelSerializer):
    id = serializers.CharField(source='idea_id')
    startup_requirements = serializers.ReadOnlyField()
    potential_customers = serializers.ReadOnlyField()
    marketing_strategies = serializers.ReadOnlyField()
    next_steps = serializers.ReadOnlyField()

    class Meta:
        model = BusinessIdea
        fields = [
            'id', 'title', 'category', 'summary',
            'capital_min_kes', 'capital_max_kes', 'capital_tier',
            'estimated_monthly_profit_min', 'estimated_monthly_profit_max',
            'rating', 'review_count',
            'startup_requirements', 'potential_customers', 'marketing_strategies', 'next_steps'
        ]

class TestimonialSerializer(serializers.ModelSerializer):
    location = serializers.CharField(source='county')

    class Meta:
        model = Testimonial
        fields = [
            'id', 'name', 'business_name', 'location',
            'quote', 'starting_capital_kes', 'current_monthly_revenue_kes', 'advice'
        ]

class RemoteEventDtoSerializer(serializers.Serializer):
    event_id = serializers.CharField(max_length=100)
    event_type = serializers.CharField(max_length=60)
    target_id = serializers.CharField(max_length=100, allow_null=True, required=False)
    target_title = serializers.CharField(max_length=200, allow_null=True, required=False)
    category = serializers.CharField(max_length=100, allow_null=True, required=False)
    timestamp_ms = serializers.IntegerField()
    metadata = serializers.DictField(child=serializers.CharField(), required=False)

class EventBatchRequestSerializer(serializers.Serializer):
    device_id = serializers.CharField(max_length=100)
    events = RemoteEventDtoSerializer(many=True)

class UserActivityLogSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserActivityLog
        fields = '__all__'
