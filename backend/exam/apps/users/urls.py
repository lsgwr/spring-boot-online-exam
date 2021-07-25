# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
from django.urls import path
from users.views import LoginView, RegisterView

urlpatterns = [
    path('login', LoginView.as_view()),
    path('register', RegisterView.as_view())
]
