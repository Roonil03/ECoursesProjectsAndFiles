from django.shortcuts import render
from django.http import HttpResponse
from .models import Menu

# Create your views here for menu.
def menu(request):
    # Fetch all records from the Menu table
    menu_items = Menu.objects.all() 
    
    # Pack the database records into a context dictionary with the key "menu"
    items_dict = {"menu": menu_items} 
    
    # Render the template and send the dictionary along with it
    return render(request, 'menu.html', items_dict)