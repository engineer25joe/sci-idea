from pathlib import Path
import textwrap

root = Path("backend_django/api")

def append_once(path, marker, content):
    existing = path.read_text(encoding="utf-8")
    if marker not in existing:
        path.write_text(
            existing.rstrip() + "\n\n" + textwrap.dedent(content).strip() + "\n",
            encoding="utf-8"
        )

# 1. Database models
append_once(
    root / "models.py",
    "class SkillCategory(models.Model):",
    """
    class SkillCategory(models.Model):
        code = models.CharField(max_length=60, unique=True)
        name = models.CharField(max_length=100)
        description = models.TextField(blank=True)
        icon_name = models.CharField(max_length=60, blank=True)
        image_url = models.URLField(blank=True)
        display_order = models.PositiveIntegerField(default=0)
        is_active = models.BooleanField(default=True)

        class Meta:
            ordering = ['display_order', 'name']
            verbose_name_plural = 'Skill categories'

        def __str__(self):
            return self.name


    class Skill(models.Model):
        id = models.CharField(max_length=100, primary_key=True)
        name = models.CharField(max_length=150)
        category = models.ForeignKey(
            SkillCategory,
            on_delete=models.PROTECT,
            related_name='skills'
        )
        description = models.TextField(blank=True)
        image_url = models.URLField(blank=True)
        display_order = models.PositiveIntegerField(default=0)
        is_active = models.BooleanField(default=True)
        created_at = models.DateTimeField(auto_now_add=True)
        updated_at = models.DateTimeField(auto_now=True)

        class Meta:
            ordering = ['display_order', 'name']

        def __str__(self):
            return self.name
    """
)

# 2. Serializers
(root / "skill_serializers.py").write_text(textwrap.dedent("""
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
""").strip() + "\n", encoding="utf-8")

# 3. API views
(root / "skill_views.py").write_text(textwrap.dedent("""
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
""").strip() + "\n", encoding="utf-8")

# 4. Admin registration
admin_file = root / "admin.py"
admin_text = admin_file.read_text(encoding="utf-8")

if "from .models import Skill, SkillCategory" not in admin_text:
    admin_text = (
        "from .models import Skill, SkillCategory\n" + admin_text
    )

if "@admin.register(SkillCategory)" not in admin_text:
    admin_text += textwrap.dedent("""

        @admin.register(SkillCategory)
        class SkillCategoryAdmin(admin.ModelAdmin):
            list_display = ('name', 'code', 'display_order', 'is_active')
            list_editable = ('display_order', 'is_active')
            search_fields = ('name', 'code')
            ordering = ('display_order',)


        @admin.register(Skill)
        class SkillAdmin(admin.ModelAdmin):
            list_display = ('name', 'category', 'display_order', 'is_active')
            list_filter = ('category', 'is_active')
            list_editable = ('display_order', 'is_active')
            search_fields = ('name', 'description', 'id')
            ordering = ('category', 'display_order')
    """)

admin_file.write_text(admin_text, encoding="utf-8")

# 5. API routes
urls_file = root / "urls.py"
urls_text = urls_file.read_text(encoding="utf-8")

if "from .skill_views import" not in urls_text:
    urls_text = (
        "from .skill_views import SkillListView, SkillCategoryListView\n"
        + urls_text
    )

if "name='api_skills'" not in urls_text:
    urls_text = urls_text.replace(
        "urlpatterns = [",
        "urlpatterns = [\n"
        "    path('skills/', SkillListView.as_view(), name='api_skills'),\n"
        "    path('skills/categories/', SkillCategoryListView.as_view(), "
        "name='api_skill_categories'),"
    )

urls_file.write_text(urls_text, encoding="utf-8")

print("Skills catalogue files configured successfully.")