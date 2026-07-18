-- 1. Create strategic non-clustered index maps to eliminate full table scans
CREATE NONCLUSTERED INDEX idx_products_category_includes 
ON Products(Category) 
INCLUDE (ProductID, ProductName);

CREATE NONCLUSTERED INDEX idx_orders_product_quantity 
ON Orders(ProductID, Quantity);

-- 2. Execute highly optimized single revised aggregation query
SELECT p.ProductName, SUM(o.Quantity) AS TotalSold
FROM Orders o
INNER JOIN Products p ON o.ProductID = p.ProductID
WHERE p.Category = 'Electronics'
GROUP BY p.ProductName
ORDER BY TotalSold DESC;
