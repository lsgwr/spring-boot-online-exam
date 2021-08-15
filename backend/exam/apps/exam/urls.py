from django.urls import path
from rest_framework.routers import SimpleRouter

from exam.views import ExamViewset

router = SimpleRouter()
router.register('exam', ExamViewset)

urlpatterns = [
]

urlpatterns += router.urls
