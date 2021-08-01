from django.utils.deprecation import MiddlewareMixin
from django.http.response import JsonResponse


class ExceptionMiddleware(MiddlewareMixin):
    def process_exception(self, request, exception):
        try:
            return JsonResponse({'code': exception.code, 'msg': exception.msg, 'data': None})
        except Exception as e:
            pass
