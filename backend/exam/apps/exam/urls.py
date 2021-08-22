from django.urls import path
from rest_framework.routers import SimpleRouter

from exam.views import ExamViewset, ExamRecordViewset

router = SimpleRouter()
router.register('exam', ExamViewset)
router.register('record', ExamRecordViewset)

urlpatterns = [
]

urlpatterns += router.urls
