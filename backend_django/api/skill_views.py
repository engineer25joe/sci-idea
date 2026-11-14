from django.db.models import Q
from rest_framework import generics
from rest_framework.permissions import IsAuthenticated
from .models import Skill, SkillCategory
from .skill_serializers import SkillSerializer, SkillCategorySerializer


class SkillListView(generics.ListAPIView):
    serializer_class = SkillSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        queryset = Skill.objects.filter(
            is_active=True,
            category__is_active=True
        ).select_related('category')

        search = self.request.query_params.get('search')
        category = self.request.query_params.get('category')

        if search:
            queryset = queryset.filter(
                Q(name__icontains=search) |
                Q(description__icontains=search) |
                Q(category__name__icontains=search)
            )

        if category:
            queryset = queryset.filter(category__code=category)

        return queryset


class SkillCategoryListView(generics.ListAPIView):
    serializer_class = SkillCategorySerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return SkillCategory.objects.filter(
            is_active=True
        ).prefetch_related('skills')
