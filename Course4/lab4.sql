CREATE DATABASE IF NOT EXISTS cm_devices;
USE cm_devices;
CREATE TABLE invoice (
    customerName VARCHAR(50), 
    orderDate DATE, 
    quantity INT, 
    price DECIMAL(10, 2)
);
SHOW tables;
SHOW columns FROM invoice;
CREATE TABLE contacts (
    accountNumber INT,
    phoneNumber VARCHAR(14),
    email VARCHAR(255)
);
SHOW tables;
SHOW columns FROM contacts;