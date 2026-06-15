Little Lemon Restaurant - Backend API

## Overview
This is the backend capstone project for the Little Lemon Restaurant. It is built using Django and Django REST Framework (DRF), providing endpoints for menu items and table bookings. The application uses TokenAuthentication and SessionAuthentication to secure sensitive endpoints.

## Database Migrations
Before running the server, ensure that your MySQL database server is running and you have a database named `littlelemon` with the credentials configured in `settings.py`.
Activate your virtual environment and run the following commands:
```bash
python manage.py makemigrations
python manage.py migrate
```

## Creating a Superuser
To access the Django Admin panel or to have an admin user for the API, create a superuser:
```bash
python manage.py createsuperuser
```

## Running the Server
```bash
python manage.py runserver
```

## API Endpoints (For Insomnia/Postman)
- `/api/menu/` - GET to list all menu items, POST to add a new menu item (requires authentication).
- `/api/menu/<int:pk>` - GET, PUT, DELETE specific menu items (requires authentication).
- `/api/bookings/` - GET, POST, PUT, DELETE table reservations (requires authentication).
- `/api/users/` - Djoser endpoint for user registration.
- `/api/token/login/` - Djoser endpoint to get an auth token by providing username and password.
