from django.urls import path
from rest_framework.routers import SimpleRouter

from question.views import QuestionViewset, QuestionSelectionView

router = SimpleRouter()
router.register('question', QuestionViewset)
urlpatterns = [
    path('selection', QuestionSelectionView.as_view())
]

urlpatterns += router.urls
