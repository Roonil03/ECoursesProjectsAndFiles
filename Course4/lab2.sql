CREATE DATABASE IF NOT EXISTS cm_devices;
USE cm_devices;

CREATE TABLE customers (
    username CHAR(9), 
    fullName VARCHAR(100), 
    email VARCHAR(255)
);

SHOW tables;
SHOW columns FROM customers;
CREATE TABLE feedback (
    feedbackID CHAR(8), 
    feedbackType VARCHAR(100), 
    comment TEXT
);

SHOW tables;
SHOW columns FROM feedback;
