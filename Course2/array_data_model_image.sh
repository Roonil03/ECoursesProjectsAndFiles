source big-data-2-env/bin/activate
ls
cd image
python3 ./dimensions.py Australia.jpg
python3 ./pixel.py Australia.jpg 0 0
python3 ./pixel.py Australia.jpg 5000 0
python3 ./pixel.py Australia.jpg 2000 2000
deactivate
