#!/bin/bash
# Script to easily migrate database and run the server

source venv/Scripts/activate
python manage.py makemigrations
python manage.py migrate
python manage.py runserver
