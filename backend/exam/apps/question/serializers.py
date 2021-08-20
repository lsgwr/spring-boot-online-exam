from re import T
from rest_framework import serializers

from users.models import UserInfo
from question.models import (
    TblQuestion,
    TblQuestionOption,
    TblQuestionType,
    TblQuestionCategory,
    TblQuestionLevel
)


class QuestionOptionSerializer(serializers.ModelSerializer):
    class Meta:
        model = TblQuestionOption
        fields = '__all__'


class QuestionTypeSerializer(serializers.ModelSerializer):
    class Meta:
        model = TblQuestionType
        fields = '__all__'


class QuestionCategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = TblQuestionCategory
        fields = '__all__'


class QuestionLevelSerializer(serializers.ModelSerializer):
    class Meta:
        model = TblQuestionLevel
        fields = '__all__'


class QuestionSingleSerializer(serializers.ModelSerializer):
    class Meta:
        model = TblQuestion
        fields = ['id', 'name']


class QuestionSerializer(serializers.ModelSerializer):
    categoryId = serializers.IntegerField(source='category_id')
    levelId = serializers.IntegerField(source='level_id')
    typeId = serializers.IntegerField(source='type_id')

    creator = serializers.SerializerMethodField()
    level = serializers.SerializerMethodField()
    type = serializers.SerializerMethodField()
    category = serializers.SerializerMethodField()
    options = serializers.SerializerMethodField()

    def get_creator(self, obj):
        try:
            user = UserInfo.objects.get(user_id=obj.creator_id)
            return user.username
        except UserInfo.DoesNotExist as e:
            return ''

    def get_level(self, obj):
        try:
            level = TblQuestionLevel.objects.get(id=obj.level_id)
            return level.description
        except TblQuestionLevel.DoesNotExist as e:
            return ''

    def get_type(self, obj):
        try:
            type = TblQuestionType.objects.get(id=obj.type_id)
            return type.description
        except TblQuestionType.DoesNotExist as e:
            return ''

    def get_category(self, obj):
        try:
            category = TblQuestionCategory.objects.get(id=obj.category_id)
            return category.name
        except TblQuestionCategory.DoesNotExist as e:
            return ''

    def get_options(self, obj):
        try:
            option_ids = obj.option_ids.split('-')
            options = TblQuestionOption.objects.filter(id__in=option_ids).all()
            options_list = []
            for item in QuestionOptionSerializer(options, many=True).data:
                if item.get('id') == obj.answer_option_ids:
                    item['answer'] = True
                else:
                    item['answer'] = False
                options_list.append(item)
            return options_list
        except Exception as e:
            pass
        return []

    class Meta:
        model = TblQuestion
        fields = '__all__'


class QuestionSelectionSerializer(serializers.Serializer):
    categories = serializers.ListField(
        child=QuestionCategorySerializer()
    )
    levels = serializers.ListField(
        child=QuestionLevelSerializer()
    )
    types = serializers.ListField(
        child=QuestionTypeSerializer()
    )


class QuestionDetailSerializer(serializers.ModelSerializer):
    type = serializers.SerializerMethodField()
    options = serializers.SerializerMethodField()

    def get_type(self, obj):
        try:
            type = TblQuestionType.objects.get(id=obj.type_id)
            return type.description
        except TblQuestionType.DoesNotExist as e:
            return ''

    def get_options(self, obj):
        try:
            option_ids = obj.option_ids.split('-')
            options = TblQuestionOption.objects.filter(id__in=option_ids).all()
            options_list = []
            for item in options:
                options_list.append({
                    'questionOptionContent': item.content,
                    'questionOptionDescription': item.description,
                    'questionOptionId': item.id
                })
            return options_list
        except Exception as e:
            pass
        return []

    class Meta:
        model = TblQuestion
        fields = ['type', 'id', 'name', 'description', 'options']