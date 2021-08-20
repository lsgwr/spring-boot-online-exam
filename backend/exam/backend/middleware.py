from django.utils.deprecation import MiddlewareMixin
from django.http.response import JsonResponse


class CustomMiddleware(MiddlewareMixin):
    def process_exception(self, request, exception):
        try:
            return JsonResponse({'code': exception.code, 'msg': exception.msg, 'data': None})
        except Exception as e:
            pass

    def process_response(self,request, response):
        # 为了适应前端格式，在服务的的每个响应消息内加上code和msg
        if hasattr(response, 'accepted_media_type') \
            and 'json' in response.accepted_media_type \
            and hasattr(response, 'data') \
            and not isinstance(response.data, dict):
            return JsonResponse({'code': 0, 'msg': '', 'data': response.data})
        return response