SELECT*  FROM Invoice ORDER BY Total DESC;

SELECT SUM(UnitPrice * Quantity) AS TotalRevenue FROM InvoiceLine;

SELECT c.CustomerId, c.FirstName, c.LastName, SUM(i.Total) AS TotalSpent FROM Customer AS c JOIN Invoice AS i ON c.CustomerId = i.CustomerId GROUP BY c.CustomerId, c.FirstName, c.LastName HAVING SUM(i.Total) > 100.00;