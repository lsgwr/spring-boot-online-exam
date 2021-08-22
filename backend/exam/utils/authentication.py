# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
import time
from django.db.models import Q
from rest_framework.authentication import BaseAuthentication
from rest_framework.exceptions import AuthenticationFailed
from users.models import UserInfo
from utils.jwt_util import JwtUtil
from threading import local

_thread_local = local()


class JwtAuthentication(BaseAuthentication):
    def authenticate(self, request):
        access_token = request.META.get('HTTP_AUTHORIZATION', None)
        if access_token:
            data = JwtUtil.check_jwt_token(access_token.split(' ')[-1])
            if data:
                username = data.get('username')
                exp = data.get('exp')
                if time.time() > exp:
                    raise AuthenticationFailed('authentication time out')

                try:
                    user = UserInfo.objects.get(username=username)
                    _thread_local.user = user
                except (UserInfo.DoesNotExist, UserInfo.MultipleObjectsReturned) as e:
                    return (None, None)
                else:
                    return (user, None)

        raise AuthenticationFailed('authentication failed')

def get_current_user():
    return getattr(_thread_local, 'user', None)
