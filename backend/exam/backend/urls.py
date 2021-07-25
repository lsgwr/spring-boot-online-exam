"""xingxing URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include, re_path
from django.views.generic.base import RedirectView
from rest_framework.documentation import include_docs_urls
from django.conf import settings
from django.views.static import serve


urlpatterns = [
    path('', RedirectView.as_view(url='docs')),
    path('docs/', include_docs_urls(title='API 文档', description='开发接口文档', authentication_classes=(),
                                    permission_classes=())),
    path('admin/', admin.site.urls),
    path('user/', include('users.urls'))
]

# 生产环境中使 API 文档也可访问
if not settings.DEBUG:
    urlpatterns += [re_path(r'^static/(?P<path>.*)$', serve, {'document_root': settings.STATIC_ROOT})]