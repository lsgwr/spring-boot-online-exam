from django.urls import path
from rest_framework.routers import SimpleRouter

from question.views import QuestionViewset

router = SimpleRouter()
router.register('question', QuestionViewset)
urlpatterns = [
]

urlpatterns += router.urls
