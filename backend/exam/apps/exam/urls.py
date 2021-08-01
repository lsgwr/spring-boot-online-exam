from django.urls import path

from exam.views import LoginView, RegisterView

urlpatterns = [
    path('question/all', LoginView.as_view()),
    path('register', RegisterView.as_view())
]