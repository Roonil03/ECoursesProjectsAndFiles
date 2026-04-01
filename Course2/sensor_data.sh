source big-data-2-env/bin/activate
cd sensor/
ls
more wx-data.txt
more wxt-520-format.txt
python3 ./plot-data.py wx-data.txt Ta
python3 ./plot-data.py wx-data.txt Pa
cd ../
deactivate
