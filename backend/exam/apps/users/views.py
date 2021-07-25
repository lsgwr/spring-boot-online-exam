# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
import logging
from django.db.models import Q
from django.contrib.auth import authenticate
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.request import Request
from rest_framework.generics import GenericAPIView

from users.models import UserInfo
from users.serializers import LoginSerivalizer, RegisterSerivalizer

LOG = logging.getLogger('request')


# Create your views here.
class LoginView(GenericAPIView):
    authentication_classes = []
    # 这里的 serializer_class 将会提供给 API 文档生成
    serializer_class = LoginSerivalizer

    def post(self, request: Request):
        ser = LoginSerivalizer(data=request.data)
        ser.is_valid(raise_exception=True)

        username = ser.data.get('username')
        password = ser.data.get('password')

        ret = {'result': 'success', 'detail': ''}
        try:
            user = authenticate(request, username=username, password=password)
            ret['detail'] = '登录成功'
            ret['token'] = user.token
        except (UserInfo.DoesNotExist, UserInfo.MultipleObjectsReturned) as e:
            ret['detail'] = '用户名或密码错误'

        return Response(ret)


class RegisterView(GenericAPIView):
    authentication_classes = []
    serializer_class = RegisterSerivalizer

    def post(self, request: Request):
        ser = RegisterSerivalizer(data=request.data)
        ser.is_valid(raise_exception=True)

        username = ser.data.get('username')
        password = ser.data.get('password')
        ret = {'result': 'failure', 'detail': ''}

        try:
            UserInfo.objects.get(Q(username=username) | Q(telephone=username))
        except (UserInfo.DoesNotExist, UserInfo.MultipleObjectsReturned) as e:
            user = UserInfo(username=username)
            user.set_password(password)

            # 我们无需额外激活账号，所以注册是自动激活
            user.is_active = True
            user.save()
            ret['result'] = 'success'
            ret['detail'] = '注册成功'
            LOG.info('用户: %s 注册成功', username)
        else:
            ret['detail'] = '用户已注册'

        return Response(ret)
