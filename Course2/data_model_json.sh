source big-data-2-env/bin/activate
cd json/
ls
more twitter.json
python3 json_schema.py twitter.json | more
echo -e "Enter filename: twitter.json\nWhich Tweet Number are you interested in ? 99\nEnter path (ex: user/id) : entities/hashtags\n[{'text': 'Beyond', 'indices': [9, 16]}]"
python3 ./print_json.py
deactivate
