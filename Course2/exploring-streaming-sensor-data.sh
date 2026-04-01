source big-data-2-env/bin/activate
cd sensor/
python3 ./stream-data.py
more wxt-520-format.txt
python3 ./stream-plot-data.py Sm
python3 ./stream-plot-data.py Ta
deactivate
