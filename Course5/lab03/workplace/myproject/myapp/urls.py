from django.urls import path
from . import views

urlpatterns = [
    # Captures the dynamic string parameter and hands it off to views.drinks
    path('drinks/<str:drink_name>', views.drinks, name="drink_name"), 
]