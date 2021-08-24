from django.db.models import fields
from rest_framework import serializers

from exam.models import TblExam, TblRecord
from users.models import UserInfo
from question.models import TblQuestion
from question.serializers import QuestionSingleSerializer
from users.serializers import UserInfoSerivalizer


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
    examDescription = serializers.CharField(max_length=256,
                                            source='description')
    examEndDate = serializers.DateTimeField(source='end_date')
    examCreatorId = serializers.CharField(max_length=40, source='creator_id')
    examId = serializers.CharField(max_length=40, source='id')
    examName = serializers.CharField(max_length=128, source='name')
    examQuestionIds = serializers.CharField(max_length=2048,
                                            source='question_ids')
    examQuestionIdsCheck = serializers.CharField(max_length=512,
                                                 source='question_ids_check')
    examQuestionIdsJudge = serializers.CharField(max_length=512,
                                                 source='question_ids_judge')
    examQuestionIdsRadio = serializers.CharField(max_length=512,
                                                 source='question_ids_radio')
    examScore = serializers.IntegerField(source='score')
    examScoreCheck = serializers.IntegerField(source='score_check')
    examScoreJudge = serializers.IntegerField(source='score_judge')
    examScoreRadio = serializers.IntegerField(source='score_radio')
    examStartDate = serializers.DateTimeField(source='start_date')
    examTimeLimit = serializers.IntegerField(source='time_limit')
    updateTime = serializers.DateTimeField(source='update_time')

    class Meta:
        model = TblExam
        fields = [
            'examAvatar', 'examDescription', 'examEndDate', 'examCreatorId',
            'examId', 'examName', 'examQuestionIds', 'examQuestionIdsCheck',
            'examQuestionIdsJudge', 'examQuestionIdsRadio', 'examScore',
            'examScoreCheck', 'examScoreJudge', 'examScoreRadio', 'examStartDate',
            'examTimeLimit', 'updateTime'
        ]


class ExamRecordSerializer(serializers.ModelSerializer):
    examRecordId = serializers.CharField(max_length=40, source='id')
    examId = serializers.CharField(max_length=40, source='exam_id')
    answerOptionIds = serializers.CharField(max_length=4096, source='answer_option_ids')
    examJoinerId = serializers.CharField(max_length=40, source='joiner_id')
    examJoinDate = serializers.DateTimeField(source='join_date')
    examTimeCost = serializers.IntegerField(source='time_cost')
    examJoinScore = serializers.IntegerField(source='join_score')
    examResultLevel = serializers.IntegerField(source='result_level')

    class Meta:
        model = TblRecord
        fields = [
            'examRecordId', 'examId', 'answerOptionIds', 'examJoinerId',
            'examJoinDate', 'examTimeCost', 'examJoinScore', 'examResultLevel'
        ]


class ExamRecordListSerializer(serializers.Serializer):
    exam = serializers.SerializerMethodField()
    examRecord = serializers.SerializerMethodField()
    user = serializers.SerializerMethodField()

    def get_exam(self, obj):
        try:
            return ExamSerializers(obj.get('exam')).data
        except Exception as e:
            pass

    def get_examRecord(self, obj):
        try:
            return ExamRecordSerializer(obj.get('record')).data
        except Exception as e:
            pass

    def get_user(self, obj):
        try:
            return UserInfoSerivalizer(obj.get('user')).data
        except Exception as e:
            return ''


class CreateExamSerializer(serializers.ModelSerializer):

    class Meta:
        model = TblExam
        exclude = ['start_date', 'end_date', 'create_time', 'update_time', 'creator_id', 'id']
