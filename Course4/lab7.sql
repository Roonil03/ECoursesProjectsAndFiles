USE bookshop;
SET SQL_SAFE_UPDATES = 0;
TRUNCATE TABLE customers;
INSERT INTO customers (customerID, customerName, customerAddress) VALUES 
(1, "Jack", "115 Old street Belfast"),
(2, "James", "24 Carlson Rd London"),
(3, "Jimmy", "12 Pine Road Manchester"),
(4, "Yasmine", "77 Oak Avenue Bristol");
SELECT * FROM customers;
DELETE FROM customers WHERE customerID = 3;
SELECT * FROM customers;
DELETE FROM customers WHERE customerName = "Yasmine";
SELECT * FROM customers;