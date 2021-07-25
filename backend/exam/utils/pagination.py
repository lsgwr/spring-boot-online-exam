from rest_framework.pagination import PageNumberPagination


class CustomPagination(PageNumberPagination):
    page_size_query_param = 'pageSize'
    page_query_param = 'pageNo'
    max_page_size = 10000