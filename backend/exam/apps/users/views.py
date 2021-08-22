# -*- coding: utf-8 -*-
""" 
@author: xingxingzaixian
@create: 2020/9/6
@description: 
"""
from django.db.models import Q
from django.contrib.auth import authenticate
from rest_framework.response import Response
from rest_framework.request import Request
from rest_framework import status as HttpStatus
from rest_framework.generics import GenericAPIView
from rest_framework.viewsets import GenericViewSet
from rest_framework.mixins import RetrieveModelMixin

from users.models import UserInfo, TblRole, TblPage, TblAction
from users.serializers import LoginSerivalizer, RegisterSerivalizer, UserInfoSerivalizer
from utils.enum import STUDENT
from utils.logger import logger


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

        ret = {'code': 0, 'msg': ''}
        try:
            user = authenticate(request, username=username, password=password)
            ret['msg'] = '登录成功'
            ret['data'] = user.token
        except Exception as e:
            ret['msg'] = '用户名或密码错误'

        return Response(ret)


class RegisterView(GenericAPIView):
    authentication_classes = []
    serializer_class = RegisterSerivalizer

    def post(self, request: Request):
        ser = RegisterSerivalizer(data=request.data)
        ser.is_valid(raise_exception=True)

        username = ser.data.get('username')
        password = ser.data.get('password')
        ret = {'code': 0, 'msg': ''}

        try:
            UserInfo.objects.get(Q(username=username) | Q(telephone=username))
        except (UserInfo.DoesNotExist, UserInfo.MultipleObjectsReturned) as e:
            user = UserInfo(
                username=username,
                role_id=STUDENT, # 默认身份为学生
                telephone=ser.data.get('telephone'))
            user.set_password(password)

            # 我们无需额外激活账号，所以注册是自动激活
            user.is_active = True
            user.save()
            ret['msg'] = '注册成功'
            logger.info('用户: %s 注册成功', username)
        else:
            ret['msg'] = '用户已注册'

        return Response(ret)


class UserInfoViewset(RetrieveModelMixin, GenericViewSet):
    queryset = UserInfo.objects.all().order_by('-create_time')
    serializer_class = UserInfoSerivalizer

    def get_object(self):
        return self.request.user


class InfoView(GenericAPIView):
    queryset = UserInfo.objects.all().order_by('-create_time')
    serializer_class = UserInfoSerivalizer

    def get(self, request, *args, **kwargs):
        ret = {'code': 0, 'msg': '', 'data': None}

        try:
            userinfo = self.get_serializer(request.user)

            # 1.设置用户信息
            result = userinfo.data
            role = TblRole.objects.filter(id=userinfo.data.get('role_id')).first()

            if not role:
                return Response(ret, status=HttpStatus.HTTP_400_BAD_REQUEST)

            # 2.设置角色名称
            result['roleId'] = role.name

            # 3.设置当前用户的角色细节
            result['role'] = {
                'id': role.name,
                'name': role.description,
                'describe': role.detail,
            }

            # 4.设置角色的可访问页面
            page_list = role.page_ids.split('-')
            permissions = []
            for page_id in page_list:
                # 4.1 向Role中添加Page
                page = TblPage.objects.filter(id=int(page_id)).first()
                item = {
                    'permissionId': page.name,
                    'permissionName': page.description,
                    'actionEntitySet': []
                }

                # 4.2 向Page中添加action
                action_list = page.action_ids.split('-')
                for action_id in action_list:
                    action = TblAction.objects.filter(id=int(action_id)).first()
                    item['actionEntitySet'].append({
                        'action': action.name,
                        'defaultCheck': action.default_check,
                        'describe': action.description
                    })
                permissions.append(item)
            result['role']['permissions'] = permissions
            ret['data'] = result
        except Exception as e:
            logger.exception(e)

        return Response(ret)