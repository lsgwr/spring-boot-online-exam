from django.db.models import fields
from rest_framework import serializers

from question.models import TblQuestion


class QuestionSerializer(serializers.ModelSerializer):
    class Meta:
        model = TblQuestion
        fields = '__all__'
