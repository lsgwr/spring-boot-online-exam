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
    user_id = models.CharField(max_length=40, default=uuid4, primary_key=True)
    nickname = models.CharField(max_length=64, verbose_name='昵称', help_text='昵称')
    role_id = models.IntegerField(verbose_name='用户ID', help_text='用户ID')
    avatar = models.CharField(max_length=256, null=True, blank=True, verbose_name='头像', help_text='头像')
    description = models.CharField(max_length=256, verbose_name='头像', null=True, blank=True, help_text='头像')
    telephone = models.CharField(max_length=11, blank=True, null=True, verbose_name='手机号码', help_text='手机号码')
    create_time = models.DateTimeField(auto_now_add=True, verbose_name='创建时间', help_text='创建时间')
    update_time = models.DateTimeField(auto_now=True, verbose_name='创建时间', help_text='创建时间')

    class Meta:
        db_table = 'user'


class TblRole(models.Model):
    name = models.CharField(max_length=64, verbose_name='昵称', help_text='昵称')
    description = models.CharField(max_length=256, null=True, blank=True, verbose_name='描述', help_text='描述')
    detail = models.CharField(max_length=64, null=True, blank=True, verbose_name='详细功能', help_text='详细功能')
    page_ids = models.CharField(max_length=64, null=True, blank=True, verbose_name='当前角色所能访问的页面的id集合', help_text='当前角色所能访问的页面的id集合')

    class Meta:
        db_table = 'role'


class TblPage(models.Model):
    name = models.CharField(max_length=64, unique=True, verbose_name='页面的名称,要唯一', help_text='页面的名称,要唯一')
    description = models.CharField(max_length=128, blank=True, null=True, verbose_name='页面的功能性描述', help_text='页面的功能性描述')
    action_ids = models.CharField(max_length=128, blank=True, null=True, verbose_name='页面对应的操作权限列表，用-连接action的id', help_text='页面对应的操作权限列表，用-连接action的id')

    class Meta:
        db_table = 'page'


class TblAction(models.Model):
    name = models.CharField(max_length=64, unique=True, verbose_name='前端操作的名字', help_text='前端操作的名字')
    description = models.CharField(max_length=128, blank=True, null=True, verbose_name='页面操作的描述', help_text='页面操作的描述')
    default_check = models.SmallIntegerField(default=0, verbose_name='当前操作是否需要校验,true为1,0为false', help_text='当前操作是否需要校验,true为1,0为false')

    class Meta:
        db_table = 'action'