CREATE DATABASE bookshop;
USE bookshop;
CREATE TABLE customers (
    customerID INT, 
    customerName VARCHAR(50), 
    customerAddress VARCHAR(255)
);
SHOW tables;
INSERT INTO customers (customerID, customerName, customerAddress) 
VALUES (1, "Jack", "115 Old street Belfast");
SELECT * FROM customers;
INSERT INTO customers (customerID, customerName, customerAddress) 
VALUES (2, "James", "24 Carlson Rd London");
SELECT * FROM customers;