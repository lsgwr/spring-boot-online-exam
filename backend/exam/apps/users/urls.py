# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
from django.urls import path
from rest_framework.routers import SimpleRouter

from users.views import LoginView, RegisterView, UserInfoViewset, InfoView


router = SimpleRouter()
router.register('userinfo', UserInfoViewset)

urlpatterns = [
    path('login', LoginView.as_view()),
    path('register', RegisterView.as_view()),
    path('info', InfoView.as_view())
]

urlpatterns += router.urls
