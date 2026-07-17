SELECT ProductName, Category, Price, StockLevel 
FROM Products;

SELECT ProductName, Category, Price, StockLevel 
FROM Products 
WHERE Category = 'Electronics';

SELECT ProductName, Category, Price, StockLevel 
FROM Products 
WHERE StockLevel < 15;

SELECT ProductName, Category, Price, StockLevel 
FROM Products 
ORDER BY Price ASC;
