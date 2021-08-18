from rest_framework.viewsets import ModelViewSet

from exam.models import TblExam
from exam.serializers import ExamSerializers, ExamListSerializers


class ExamViewset(ModelViewSet):
    queryset = TblExam.objects.all().order_by('create_time')
    serializer_class = ExamSerializers

    def get_serializer_class(self):
        if self.action == 'list':
            self.serializer_class = ExamListSerializers
        return super().get_serializer_class()
