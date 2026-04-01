#!/usr/bin/python

import sys
import re
import time
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime
from pytz import timezone
import socket

minVal = 0
maxVal = 1

x = []
y = []

#fig, ax = plt.subplots()
fig = plt.figure()
ax = fig.add_subplot(111)
ax.set_autoscaley_on(False)
ax.set_ylim(minVal,maxVal)

plt.ion()

plt.xlabel('time')
plt.ylabel(sys.argv[1])

date_formatter = mdates.DateFormatter('%H:%M.%S', tz=timezone('US/Pacific'))

#fig.show()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(('rtd.hpwren.ucsd.edu', 12024))
for i in range(0, 60):
    line = s.recv(1024).decode()
    parts = re.split("\s+", line)

    data = parts[3].split(",")

    for field in data:
        match = re.match(sys.argv[1] + '=(\d+\.?\d+).*', field)
        if match:
            timestamp = datetime.fromtimestamp(float(parts[2]))

            #print(timestamp)
            #print(match.group(1))
            
            val = float(match.group(1))
            if val < minVal:
                minVal = val - 1
                ax.set_ylim(minVal,maxVal)
            elif val > maxVal:
                maxVal = val + 1
                ax.set_ylim(minVal,maxVal)
            
            x.append(timestamp)
            y.append(val)
            
            secs = mdates.date2num(x)
            ax.plot_date(secs, y, fmt='.-', color='blue', markersize=10)
            ax.xaxis.set_major_formatter(date_formatter)
            fig.autofmt_xdate()
            plt.draw()
            plt.pause(0.1)
s.close()

plt.ioff() 
plt.show()