# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
import logging
from django.contrib.auth import backends
from django.db.models import Q

from users.models import UserInfo
from utils.jwt_util import JwtUtil
from utils.execption import AuthenticateException

logger = logging.getLogger('request')


class UserBackend(backends.ModelBackend):
    def authenticate(self, request, username=None, password=None, **kwargs):
        try:
            user = UserInfo.objects.get(Q(username=username) | Q(telephone=username))
            if user.check_password(password):
                jwt = JwtUtil()
                token = jwt.gen_jwt_token(user)
                user.token = token
                return user
        except UserInfo.DoesNotExist as e:
            logger.error(f'[username={username}]: {e}')

        raise AuthenticateException(detail="SysUserTbl matching query does not exist.")
