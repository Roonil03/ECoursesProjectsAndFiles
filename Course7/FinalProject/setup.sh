#!/bin/bash
pipenv shell
pipenv install django mysqlclient
python manage.py makemigrations
python manage.py migrate
python manage.py runserver
