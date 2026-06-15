CREATE DATABASE cm_devices;
USE cm_devices;
CREATE TABLE devices (
    deviceID INT, 
    deviceName VARCHAR(50), 
    price DECIMAL(10, 2)
);
SHOW tables;
SHOW columns FROM devices;

CREATE TABLE stock (
    deviceID INT, 
    quantity INT, 
    totalPrice DECIMAL(10, 2)
);
SHOW tables;
SHOW columns FROM stock;
