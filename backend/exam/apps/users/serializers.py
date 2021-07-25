# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
import re
from rest_framework import serializers
from rest_framework.exceptions import ValidationError


class RegxSerivalizer:
    def __init__(self, message):
        if message is not None:
            self.message = message

    def __call__(self, value):
        regx = re.compile(r'[^\w\d_]')
        if regx.search(value):
            raise ValidationError(detail=self.message)


class LoginSerivalizer(serializers.Serializer):
    username = serializers.CharField(max_length=64, validators=[RegxSerivalizer(message='用户只能包含字母数字下划线')], help_text='用户名')
    password = serializers.CharField(min_length=8, max_length=32, validators=[RegxSerivalizer(message='密码只能包含字母数字下划线')], help_text='密码')


class RegisterSerivalizer(serializers.Serializer):
    username = serializers.CharField(max_length=64, validators=[RegxSerivalizer(message='用户只能包含字母数字下划线')], help_text='用户名')
    password = serializers.CharField(min_length=8, max_length=32, validators=[RegxSerivalizer(message='密码只能包含字母数字下划线')], help_text='密码')
    confirm = serializers.CharField(min_length=8, max_length=32, validators=[RegxSerivalizer(message='密码只能包含字母数字下划线')], help_text='确认密码')

    def validate(self, attrs):
        if attrs.get('password') != attrs.get('confirm'):
            raise ValidationError(detail='密码不一致')

        return attrs
