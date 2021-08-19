# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
import time

from jose import jwt
from django.conf import settings

from utils.logger import logger


class JwtUtil:
    @staticmethod
    def gen_jwt_token(user):
        to_encode = {
            'username': user.username,
            'exp': time.time() + 24 * 3600,
        }

        encoded_jwt = jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)
        return encoded_jwt

    @staticmethod
    def check_jwt_token(value):
        playload = {}
        try:
            playload = jwt.decode(value, settings.SECRET_KEY, algorithms=[settings.ALGORITHM])
        except Exception as e:
            logger.error(e)

        return playload
