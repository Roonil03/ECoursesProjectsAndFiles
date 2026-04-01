import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(('rtd.hpwren.ucsd.edu', 12024))
for i in range(0, 60):
    d = s.recv(1024).decode()
    p = d.split('\t', 2)
    print("{0}: {1}".format(i, p[2]))
s.close()