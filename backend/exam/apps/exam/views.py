from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.viewsets import ModelViewSet

from exam.models import TblExam, TblRecord
from exam.serializers import (
    ExamSerializers,
    ExamListSerializers,
    ExamAllSerializers,
    ExamRecordSerializer,
    ExamRecordListSerializer,
    CreateExamSerializer
)
from question.models import TblQuestion


class ExamViewset(ModelViewSet):
    queryset = TblExam.objects.all().order_by('-create_time')
    serializer_class = ExamSerializers

    def get_serializer_class(self):
        if self.action == 'create':
            self.serializer_class = CreateExamSerializer
        elif self.action == 'list':
            self.serializer_class = ExamListSerializers
        elif self.action == 'all':
            self.serializer_class = ExamAllSerializers
        return super().get_serializer_class()

    @action(methods=['GET'], detail=False, url_path='list')
    def get_list(self, request, pk=None, *args, **kwargs):
        serializer = self.get_serializer(self.queryset, many=True)
        return Response(serializer.data)

    @action(methods=['GET'], detail=False, url_path='all')
    def get_all(self, request, pk=None, *args, **kwargs):
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

    def create(self, request, *args, **kwargs):
        radios = request.data.get('radios')
        checks = request.data.get('checks')
        judges = request.data.get('judges')
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        # 计算总分
        radio_score = len(radios) * serializer.validated_data.get('score_radio')
        check_score = len(checks) * serializer.validated_data.get('score_check')
        judge_score = len(judges) * serializer.validated_data.get('score_judge')
        score = radio_score + check_score + judge_score

        # 计算总问题数
        radis_ids = '-'.join([item.get('id') for item in radios])
        check_ids = '-'.join([item.get('id') for item in checks])
        judge_ids = '-'.join([item.get('id') for item in judges])
        ids = '-'.join([radis_ids, check_ids, judge_ids])
        serializer.save(
            creator_id=request.user.user_id,
            score=score,
            question_ids=ids,
            question_ids_radio=radis_ids,
            question_ids_check=check_ids,
            question_ids_judge=judge_ids
            )
        return Response({'code': 0, 'msg': '创建考试成功', 'data': None})


class ExamRecordViewset(ModelViewSet):
    queryset = TblRecord.objects.all()
    serializer_class = ExamRecordSerializer

    def list(self, request, *args, **kwargs):
        items = []
        for record in TblRecord.objects.filter(joiner_id=request.user.user_id).all():
            exam = TblExam.objects.get(id=record.exam_id)
            items.append({
                'exam': exam,
                'record': record,
                'user': request.user
            })

        serializer = ExamRecordListSerializer(items, many=True)
        return Response({'code': 0, 'msg': '获取我的考试记录成功', 'data': serializer.data})

    def retrieve(self, request, *args, **kwargs):
        result = {}
        instance = self.get_object()
        result['examRecord'] = ExamRecordSerializer(instance).data

        answers_map = {}
        results_map = {}
        for question in instance.answer_option_ids.split('$'):
            question_result_option = question.split('_')
            question_result = question_result_option[0].split('@')
            question_options = question_result_option[1].split('-')
            answers_map[question_result[0]] = question_options
            results_map[question_result[0]] = question_result[0]
        result['answersMap'] = answers_map
        result['resultsMap'] = results_map

        exam = TblExam.objects.get(id=instance.exam_id)
        question_ids = []
        question_ids.extend(exam.question_ids_radio.split('-'))
        question_ids.extend(exam.question_ids_check.split('-'))
        question_ids.extend(exam.question_ids_judge.split('-'))
        answers_right_map = {}
        for _id in question_ids:
            question = TblQuestion.objects.get(id=_id)
            answer_option_ids = question.answer_option_ids.strip('-').split('-')
            answers_right_map[question.id] = answer_option_ids
        result['answersRightMap'] = answers_right_map
        return Response({'code': 0, 'msg': '获取考试记录详情成功', 'data': result})