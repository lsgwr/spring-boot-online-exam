from rest_framework.viewsets import ModelViewSet

from question.models import TblQuestion
from question.serializers import QuestionSerializer


class QuestionViewset(ModelViewSet):
    queryset = TblQuestion.objects.all()
    serializer_class = QuestionSerializer