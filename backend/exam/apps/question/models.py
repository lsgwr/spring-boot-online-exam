from uuid import uuid4

from django.db import models


class TblQuestion(models.Model):
    id = models.CharField(max_length=40, default=uuid4, primary_key=True)
    name = models.TextField(verbose_name='题目的名字', help_text='题目的名字')
    score = models.IntegerField(default=0, verbose_name='题目的分数', help_text='题目的分数')
    creator_id = models.CharField(max_length=40, verbose_name='题目创建者的用户id', help_text='题目创建者的用户id')
    level_id = models.IntegerField(default=0, verbose_name='题目难易度级别', help_text='题目难易度级别')
    type_id = models.IntegerField(default=0, verbose_name='题目的类型，比如单选、多选、判断等', help_text='题目的类型，比如单选、多选、判断等')
    category_id = models.IntegerField(default=0, verbose_name='题目的类型，比如数学、英语、政治等', help_text='题目的类型，比如数学、英语、政治等')
    description = models.TextField(verbose_name='题目额外的描述', help_text='题目额外的描述')
    option_ids = models.CharField(max_length=1024, verbose_name='题目的选项，用选项的id用-连在一起表示答案', help_text='题目的选项，用选项的id用-连在一起表示答案')
    answer_option_ids = models.CharField(max_length=1024, verbose_name='题目的答案，用选项的id用-连在一起表示答案', help_text='题目的答案，用选项的id用-连在一起表示答案')
    create_time = models.DateTimeField(auto_now_add=True, verbose_name='创建时间', help_text='创建时间')
    update_time = models.DateTimeField(auto_now=True, verbose_name='更新时间', help_text='更新时间')

    class Meta:
        db_table = 'question'


class TblQuestionType(models.Model):
    name = models.CharField(max_length=64, verbose_name='题目类型名称', help_text='题目类型名称')
    description = models.CharField(max_length=128, blank=True, null=True, verbose_name='题目类型描述', help_text='题目类型描述')

    class Meta:
        db_table = 'question_type'


class TblQuestionOption(models.Model):
    id = models.CharField(max_length=40, default=uuid4, primary_key=True)
    content = models.CharField(max_length=512, verbose_name='选项的内容', help_text='选项的内容')
    description = models.CharField(max_length=512, blank=True, null=True, verbose_name='选项的额外描述，可以用于题目答案解析', help_text='选项的额外描述，可以用于题目答案解析')

    class Meta:
        db_table = 'question_option'


class TblQuestionLevel(models.Model):
    name = models.CharField(max_length=64, verbose_name='题目难易度名称', help_text='题目难易度名称')
    description = models.CharField(max_length=128, verbose_name='题目难易度的描述', help_text='题目难易度的描述')

    class Meta:
        db_table = 'question_level'


class TblQuestionCategory(models.Model):
    name = models.CharField(max_length=64, verbose_name='问题类别名称', help_text='问题类别名称')
    description = models.CharField(max_length=512, blank=True, null=True, verbose_name='问题类别的描述', help_text='问题类别的描述')

    class Meta:
        db_table = 'question_category'