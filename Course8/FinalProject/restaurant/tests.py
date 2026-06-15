from django.test import TestCase
from rest_framework.test import APIClient
from .models import MenuItem

class MenuItemTest(TestCase):
    def test_get_item(self):
        item = MenuItem.objects.create(title="IceCream", price=80, inventory=100)
        self.assertEqual(str(item), "IceCream")
        self.assertEqual(item.price, 80)
        self.assertEqual(item.inventory, 100)

class MenuViewTest(TestCase):
    def setUp(self):
        self.client = APIClient()
        MenuItem.objects.create(title="Pizza", price=12.50, inventory=50)
        MenuItem.objects.create(title="Burger", price=8.50, inventory=30)

    def test_getall(self):
        response = self.client.get('/api/menu/')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.data), 2)
        self.assertEqual(response.data[0]['title'], "Pizza")
        self.assertEqual(response.data[1]['title'], "Burger")
