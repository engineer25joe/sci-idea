from django.contrib import admin
from django.urls import path, include
from api.views import web_admin_dashboard

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', web_admin_dashboard, name='root_dashboard'),
    path('dashboard/', web_admin_dashboard, name='web_admin_dashboard'),
    path('api/v1/', include('api.urls')),
]
