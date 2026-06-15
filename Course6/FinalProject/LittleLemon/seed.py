import os
import django

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'LittleLemon.settings')
django.setup()

from django.contrib.auth.models import User, Group

manager_group, _ = Group.objects.get_or_create(name='Manager')
delivery_group, _ = Group.objects.get_or_create(name='Delivery crew')

if not User.objects.filter(username='admin').exists():
    User.objects.create_superuser('admin', 'admin@example.com', 'adminpass123')

manager_user, created = User.objects.get_or_create(username='manager1', email='manager@example.com')
if created:
    manager_user.set_password('managerpass123')
    manager_user.save()
manager_user.groups.add(manager_group)

delivery_user, created = User.objects.get_or_create(username='delivery1', email='delivery@example.com')
if created:
    delivery_user.set_password('deliverypass123')
    delivery_user.save()
delivery_user.groups.add(delivery_group)

customer_user, created = User.objects.get_or_create(username='customer1', email='customer@example.com')
if created:
    customer_user.set_password('customerpass123')
    customer_user.save()

print("Seeding complete.")
