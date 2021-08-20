from rest_framework.generics import GenericAPIView
from rest_framework.viewsets import ModelViewSet
from rest_framework.response import Response

from question.models import TblQuestion, TblQuestionCategory, TblQuestionLevel, TblQuestionType
from question.serializers import QuestionSerializer, QuestionSelectionSerializer, QuestionDetailSerializer


class QuestionViewset(ModelViewSet):
    queryset = TblQuestion.objects.all().order_by('-create_time')
    serializer_class = QuestionSerializer

    def get_serializer_class(self):
        if self.action == 'retrieve':
            self.serializer_class = QuestionDetailSerializer
        return super().get_serializer_class()

    def retrieve(self, request, *args, **kwargs):
        instance = self.get_object()
        serializer = self.get_serializer(instance)
        return Response({'code': 0, 'msg': '', 'data': serializer.data})


class QuestionSelectionView(GenericAPIView):
    serializer_class = QuestionSelectionSerializer

    def get(self, request, *args, **kwargs):
        instance = {
            'categories': TblQuestionCategory.objects.all(),
            'levels': TblQuestionLevel.objects.all(),
            'types': TblQuestionType.objects.all()
        }

        serializer = self.get_serializer(instance)
        return Response({'code': 0, 'msg': '', 'data': serializer.data})