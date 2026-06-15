from django.contrib import admin
from .models import DrinksCategory, Drinks
# Import the models from models.py

# Register your models here.
admin.site.register(DrinksCategory)
admin.site.register(Drinks)