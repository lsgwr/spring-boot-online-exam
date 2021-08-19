from rest_framework import serializers

from exam.models import TblExam
from users.models import UserInfo
from question.models import TblQuestion
from question.serializers import QuestionSingleSerializer


class ExamListSerializers(serializers.ModelSerializer):
    title = serializers.CharField(source='name', max_length=128)
    elapse = serializers.IntegerField(source='time_limit')
    content = serializers.CharField(source='description', max_length=256)

    class Meta:
        model = TblExam
        fields = ['avatar', 'content', 'elapse', 'score', 'title', 'id']


class ExamAllSerializers(serializers.ModelSerializer):
    desc = serializers.CharField(max_length=256, source='description')
    radioScore = serializers.IntegerField(source='score_radio')
    checkScore = serializers.IntegerField(source='score_check')
    judgeScore = serializers.IntegerField(source='score_judge')
    elapse = serializers.IntegerField(source='time_limit')
    startDate = serializers.DateTimeField(source='start_date')
    endDate = serializers.DateTimeField(source='end_date')

    creator = serializers.SerializerMethodField()
    radios = serializers.SerializerMethodField()
    checks = serializers.SerializerMethodField()
    judges = serializers.SerializerMethodField()

    def get_creator(self, obj):
        try:
            user = UserInfo.objects.get(user_id=obj.creator_id)
            return user.username
        except UserInfo.DoesNotExist as e:
            return ''

    def get_question_data(self, ids):
        try:
            radios = TblQuestion.objects.filter(id__in=ids).all()
            radios_list = []
            for item in QuestionSingleSerializer(radios, many=True).data:
                item['checked'] = True
                radios_list.append(item)
            return radios_list
        except Exception as e:
            return []

    def get_radios(self, obj):
        return self.get_question_data(obj.question_ids_radio.split('-'))

    def get_checks(self, obj):
        return self.get_question_data(obj.question_ids_check.split('-'))

    def get_judges(self, obj):
        return self.get_question_data(obj.question_ids_judge.split('-'))

    class Meta:
        model = TblExam
        exclude = [
            'description', 'question_ids', 'question_ids_radio',
            'question_ids_check', 'question_ids_judge', 'score_radio',
            'score_check', 'score_judge', 'creator_id', 'time_limit',
            'start_date', 'end_date', 'create_time', 'update_time'
        ]

class ExamSerializers(serializers.ModelSerializer):
    examAvatar = serializers.CharField(max_length=2049, source='avatar')
    examDescription = serializers.CharField(max_length=256, source='description')
    examCreatorId = serializers.SerializerMethodField()
    class Meta:
        model = TblExam
        fields = ['checkIds', 'exam', 'judgeIds', 'radioIds']


class ExamDetailSerializers(serializers.ModelSerializer):
    checkIds = serializers.SerializerMethodField()
    exam = ExamSerializers()
    judgeIds = serializers.SerializerMethodField()
    radioIds = serializers.SerializerMethodField()

    def get_checkIds(self, obj):
        return obj.question_ids_check.split('-')

    def get_judgeIds(self, obj):
        return obj.question_ids_judge.split('-')

    def get_radioIds(self, obj):
        return obj.question_ids_radio.split('-')

    class Meta:
        model = TblExam
        fields = ['checkIds', 'exam', 'judgeIds', 'radioIds']
