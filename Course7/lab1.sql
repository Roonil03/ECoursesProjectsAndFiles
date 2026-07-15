create database bookshop;

use bookshop;
create table customers(
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    customerName VARCHAR(100) NOT NULL,
    customerAddress VARCHAR(255) NOT NULL
)