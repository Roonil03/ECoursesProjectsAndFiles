from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse

# Step 2: Define the view function to receive request and the path converter variable
def drinks(request, drink_name):
    
    # Step 3: Dictionary holding the item descriptions
    drink = {
        'mocha': 'type of coffee',
        'tea': 'type of beverage',
        'lemonade': 'type of refreshment',
    }
    
    # Step 4 & 5: Fetch the value and return it inside an HTML string response
    # Using .get() keeps the server from crashing if you type an unlisted drink name!
    choice_of_drink = drink.get(drink_name, "information not found for this item")
    
    return HttpResponse(f"<h2> {drink_name} </h2>" + choice_of_drink)