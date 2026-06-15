from django.shortcuts import render
from django.http import HttpResponse

# Create your views here.
# Home page view
def home(request):
    return HttpResponse("Welcome to Little Lemon!")

# About Us page view
def about(request):
    return HttpResponse("About us")

# Menu page view
def menu(request):
    return HttpResponse("Menu")

# Booking page view
def book(request):
    return HttpResponse("Make a booking")