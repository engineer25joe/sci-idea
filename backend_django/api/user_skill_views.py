from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework import status

from .models import UserSkillSelection
from .user_skill_serializers import UserSkillSelectionSerializer


class UserSkillSelectionView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        selection, _ = UserSkillSelection.objects.get_or_create(
            user=request.user
        )

        serializer = UserSkillSelectionSerializer(selection)
        return Response(serializer.data)

    def put(self, request):
        selection, _ = UserSkillSelection.objects.get_or_create(
            user=request.user
        )

        serializer = UserSkillSelectionSerializer(
            selection,
            data=request.data
        )

        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(serializer.data, status=status.HTTP_200_OK)