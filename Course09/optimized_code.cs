using System;
using System.Collections.Generic;
using System.Linq;

namespace WarehouseX.Optimization
{
    public class OptimizedProcessor
    {
        public void ProcessOrdersEfficiently(List<Order> orders, DatabaseContext db)
        {
            // Extract distinct Product IDs to pre-fetch records in a single batch query
            var distinctProductIds = orders.Select(o => o.ProductId).Distinct().ToList();
            
            // Build an in-memory dictionary lookup to resolve the N+1 database call bug
            var productLookup = db.Products
                .Where(p => distinctProductIds.Contains(p.Id))
                .ToDictionary(p => p.Id, p => p.Name);

            foreach (var order in orders)
            {
                if (productLookup.TryGetValue(order.ProductId, out var productName))
                {
                    Console.WriteLine($"Order {order.Id}: {productName} - {order.Quantity}");
                }
            }
        }
    }

    public class Order { public int Id { get; set; } public int ProductId { get; set; } public int Quantity { get; set; } }
    public class Product { public int Id { get; set; } public string Name { get; set; } = string.Empty; }
    public class DatabaseContext { public List<Product> Products { get; set; } = new(); }
}
