from myapp.models import DrinksCategory

# Instantiate and save the new category row record
cat = DrinksCategory(category_name='coffee')
cat.save()

# Import both model schemas into the environment context
from myapp.models import DrinksCategory, Drinks

# Retrieve the parent category record we just created (Primary Key = 1)
fk = DrinksCategory.objects.get(pk=1)

# Instantiate the drink object by passing the category instance into our foreign key field
drink = Drinks(drink='mocha', price=7, category_id=fk)

# Save the related object to update the underlying tables
drink.save()

# Exit the Django shell when completed
exit()