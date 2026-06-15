mkdir workshops
cd workshops
pip3 install virtualenv
~/.local/bin/virtualenv django-venv
source django-venv/bin/activate
pip3 install django
mkdir myproject
cd myproject
python3 manage.py startapp myapp
python3 manage.py runserver
