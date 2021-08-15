from django.urls.conf import include
from rest_framework import serializers

from exam.models import TblExam


class ExamListSerializers(serializers.ModelSerializer):
    id = serializers.CharField(source='exam_id', max_length=40, read_only=True)
    title = serializers.CharField(source='name', max_length=128)
    elapse = serializers.IntegerField(source='time_limit')
    content = serializers.CharField(source='description', max_length=256)
    class Meta:
        model = TblExam
        fields = ['avatar', 'content', 'elapse', 'score', 'title', 'id']


class ExamSerializers(serializers.ModelSerializer):
    class Meta:
        model = TblExam
        fields = '__all__'
