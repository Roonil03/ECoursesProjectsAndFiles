using System;

namespace WarehouseX.Debugging
{
    public class OrderManager
    {
        public void ProcessOrder(Order order, DatabaseContext db)
        {
            // Guard clause to prevent NullReferenceExceptions on the root object parameter
            if (order == null)
            {
                throw new ArgumentNullException(nameof(order), "Order payload cannot be null.");
            }

            var product = db.Products.Find(order.ProductId);
            
            // Validate product record existence
            if (product == null)
            {
                throw new KeyNotFoundException($"Product lookup failed for ID: {order.ProductId}.");
            }

            // Enforce stock availability requirements to handle inventory edge cases safely
            if (product.Stock < order.Quantity)
            {
                throw new InvalidOperationException($"Insufficient inventory stock for Product ID {order.ProductId}. Available: {product.Stock}, Requested: {order.Quantity}.");
            }

            product.Stock -= order.Quantity;
            db.SaveChanges();
            
            Console.WriteLine($"Order {order.Id} processed.");
        }
    }

    public class Order { public int Id { get; set; } public int ProductId { get; set; } public int Quantity { get; set; } }
    public class Product { public int Id { get; set; } public string Name { get; set; } = string.Empty; public int Stock { get; set; } }
    public class DatabaseContext { public DbSet Products { get; set; } = new(); public void SaveChanges() {} }
    public class DbSet { public Product? Find(int id) => null; }
}
