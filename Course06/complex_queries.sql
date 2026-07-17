SELECT 
    p.ProductName, 
    s.SaleDate, 
    st.StoreLocation, 
    s.UnitsSold
FROM Sales s
INNER JOIN Products p ON s.ProductID = p.ProductID
INNER JOIN Stores st ON s.StoreID = st.StoreID
INNER JOIN Suppliers sup ON p.SupplierID = sup.SupplierID;

SELECT 
    p.ProductID,
    p.ProductName,
    SUM(s.UnitsSold * p.Price) AS TotalRevenue,
    SUM(s.UnitsSold) AS TotalUnitsSold,
    AVG(s.UnitsSold) AS AverageUnitsPerSale,
    MAX(s.UnitsSold) AS PeakTransactionVolume
FROM Products p
LEFT JOIN Sales s ON p.ProductID = s.ProductID
GROUP BY p.ProductID, p.ProductName;

SELECT 
    sup.SupplierID, 
    sup.SupplierName,
    (SELECT COUNT(*) 
     FROM Shipments sh 
     WHERE sh.SupplierID = sup.SupplierID 
       AND sh.DeliveryStatus = 'Delayed') AS DelayedDeliveriesCount
FROM Suppliers sup
WHERE sup.SupplierID IN (
    SELECT SupplierID 
    FROM Shipments 
    WHERE DeliveryStatus = 'Delayed'
    GROUP BY SupplierID
    HAVING COUNT(*) > 5
)
ORDER BY DelayedDeliveriesCount DESC;

CREATE INDEX idx_products_category_stock ON Products(Category, StockLevel);
CREATE INDEX idx_sales_product_store ON Sales(ProductID, StoreID);
CREATE INDEX idx_shipments_supplier_status ON Shipments(SupplierID, DeliveryStatus);