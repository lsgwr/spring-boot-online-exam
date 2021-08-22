from uuid import uuid4

from django.db import models


class TblExam(models.Model):
    id = models.CharField(max_length=40, default=uuid4, primary_key=True)
    name = models.CharField(max_length=128, verbose_name='考试名称', help_text='考试名称')
    avatar = models.TextField(verbose_name='考试的预览图', help_text='考试的预览图')
    description = models.CharField(max_length=256, blank=True, null=True, verbose_name='考试描述', help_text='考试描述')
    question_ids = models.CharField(max_length=2048, blank=True, null=True, verbose_name='当前考试下的题目的id用-连在一起地字符串', help_text='当前考试下的题目的id用-连在一起地字符串')
    question_ids_radio = models.CharField(max_length=512, blank=True, null=True, verbose_name='当前考试下的题目单选题的id', help_text='当前考试下的题目单选题的id')
    question_ids_check = models.CharField(max_length=512, blank=True, null=True, verbose_name='当前考试下的题目多选题的id', help_text='当前考试下的题目多选题的id')
    question_ids_judge = models.CharField(max_length=512, blank=True, null=True, verbose_name='当前考试下的题目判断题的id', help_text='当前考试下的题目判断题的id')
    score = models.IntegerField(default=0, verbose_name='当前考试的总分数', help_text='当前考试的总分数')
    score_radio = models.IntegerField(default=0, verbose_name='当前考试每个单选题的分数', help_text='当前考试每个单选题的分数')
    score_check = models.IntegerField(default=0, verbose_name='当前考试每个多选题的分数', help_text='当前考试每个多选题的分数')
    score_judge = models.IntegerField(default=0, verbose_name='当前考试每个判断题的分数', help_text='当前考试每个判断题的分数')
    creator_id = models.CharField(max_length=40, verbose_name='考试创建者的用户id', help_text='考试创建者的用户id')
    time_limit = models.IntegerField(default=0, verbose_name='考试的时间限制，单位为分钟', help_text='考试的时间限制，单位为分钟')
    start_date = models.DateTimeField(verbose_name='考试有效期开始时间', help_text='考试有效期开始时间')
    end_date = models.DateTimeField(verbose_name='考试有效期结束时间', help_text='考试有效期结束时间')
    create_time = models.DateTimeField(auto_now_add=True, verbose_name='创建时间', help_text='创建时间')
    update_time = models.DateTimeField(auto_now=True, verbose_name='更新时间', help_text='更新时间')

    class Meta:
        db_table = 'exam'


class TblRecord(models.Model):
    id = models.CharField(max_length=40, default=uuid4, primary_key=True)
    joiner_id = models.CharField(max_length=40, verbose_name='考试参与者的用户id', help_text='考试参与者的用户id')
    join_date = models.DateTimeField(auto_now_add=True, verbose_name='参加考试的时间', help_text='参加考试的时间')
    time_cost = models.IntegerField(default=0, verbose_name='完成考试所用的时间,单位分钟', help_text='完成考试所用的时间,单位分钟')
    join_score = models.IntegerField(default=0, verbose_name='参与考试的实际得分', help_text='参与考试的实际得分')
    result_level = models.IntegerField(default=0, verbose_name='考试结果的等级', help_text='考试结果的等级')
    answer_option_ids = models.CharField(max_length=4096, verbose_name='答案选项', help_text='答案选项')
    exam_id = models.CharField(max_length=40, verbose_name='考试ID', help_text='考试ID')

    class Meta:
        db_table = 'exam_record'


class TblRecordLevel(models.Model):
    name = models.CharField(max_length=128, verbose_name='考试结果等级的名称', help_text='考试结果等级的名称')
    description = models.CharField(max_length=512, blank=True, null=True, verbose_name='考试结果等级的详细阐述', help_text='考试结果等级的详细阐述')

    class Meta:
        db_table = 'exam_record_level'