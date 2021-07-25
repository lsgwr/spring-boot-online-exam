from django.utils.deprecation import MiddlewareMixin
from django.http.response import JsonResponse


class ExceptionMiddleware(MiddlewareMixin):
    def process_exception(self, request, exception):
        try:
            # 如果是身份认证失败
            return JsonResponse({'code': exception.code, 'msg': exception.msg, 'detail': exception.detail})
        except Exception as e:
            pass
