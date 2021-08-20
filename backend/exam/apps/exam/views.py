from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.viewsets import ModelViewSet

from exam.models import TblExam
from exam.serializers import ExamSerializers, ExamListSerializers, ExamAllSerializers


class ExamViewset(ModelViewSet):
    queryset = TblExam.objects.all().order_by('-create_time')
    serializer_class = ExamSerializers

    @action(methods=['GET'], detail=False, url_path='list')
    def get_list(self, request, pk=None, *args, **kwargs):
        self.serializer_class = ExamListSerializers
        serializer = self.get_serializer(self.queryset, many=True)
        return Response(serializer.data)

    @action(methods=['GET'], detail=False, url_path='all')
    def get_all(self, request, pk=None, *args, **kwargs):
        self.serializer_class = ExamAllSerializers
        serializer = self.get_serializer(self.queryset, many=True)
        return Response(serializer.data)

    def retrieve(self, request, *args, **kwargs):
        instance = self.get_object()
        serializer = self.get_serializer(instance)
        data = {
            'checkIds': serializer.data.get('examQuestionIdsCheck').split('-'),
            'judgeIds': serializer.data.get('examQuestionIdsJudge').split('-'),
            'radioIds': serializer.data.get('examQuestionIdsRadio').split('-'),
            'exam': serializer.data
        }

        return Response({'code': 0, 'msg': 0, 'data': data})