# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
from django.contrib.auth.models import AbstractUser
from django.db import models
from uuid import uuid4


# Create your models here.

class UserInfo(AbstractUser):
    uid = models.CharField(max_length=36, default=uuid4, primary_key=True)
    nickname = models.CharField(max_length=32, verbose_name='昵称')
    telephone = models.CharField(max_length=11, blank=True, null=True, unique=True, verbose_name='手机号码')
    create_time = models.DateTimeField(auto_now_add=True, verbose_name='创建时间')

    class Meta:
        db_table = 'tbl_user'
