using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Encodings.Web;
using System.Text.RegularExpressions;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Caching.Memory;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddMemoryCache();

var app = builder.Build();

// Simulating database storage context
var mockProductDatabase = new List<ProductItem>
{
    new() { Id = 1, Name = "Industrial Laptop", Price = 1500.00, Stock = 45, Category = new() { Id = 101, Name = "Electronics" } },
    new() { Id = 2, Name = "Wireless Headset", Price = 89.99, Stock = 120, Category = new() { Id = 102, Name = "Accessories" } }
};

// Route 1: Retrieve all items with in-memory caching optimization
app.MapGet("/api/products", (IMemoryCache cache) =>
{
    const string CacheKey = "CatalogList";

    if (!cache.TryGetValue(CacheKey, out List<ProductItem>? cachedProducts))
    {
        cachedProducts = mockProductDatabase;
        var cacheOptions = new MemoryCacheEntryOptions()
            .SetAbsoluteExpiration(TimeSpan.FromMinutes(10))
            .SetSlidingExpiration(TimeSpan.FromMinutes(2));
        cache.Set(CacheKey, cachedProducts, cacheOptions);
    }

    return Results.Ok(cachedProducts);
});

// Route 2: Add secure data item with validation and explicit role checking simulations
app.MapPost("/api/products", (ProductInput input, IMemoryCache cache) =>
{
    // 1. Authentication Check Simulation
    if (string.IsNullOrEmpty(input.UserToken))
    {
        return Results.Json(new { error = "Unauthorized access attempt detected." }, statusCode: 401);
    }

    // 2. Role-Based Access Control Simulation (Admin Only check)
    if (input.UserRole != "Admin")
    {
        return Results.Json(new { error = "Forbidden. Elevated administrative access privileges required." }, statusCode: 403);
    }

    // 3. Input Validation Constraints
    var alphanumericRegex = new Regex(@"^[a-zA-Z0-9\s-_]{3,50}$");
    if (!alphanumericRegex.IsMatch(input.Name) || input.Price <= 0 || input.Stock < 0)
    {
        return Results.BadRequest("Invalid text payload input structure or numerical value ranges.");
    }

    // 4. XSS Sanitization Execution
    string safeName = HtmlEncoder.Default.Encode(input.Name);

    var newItem = new ProductItem
    {
        Id = mockProductDatabase.Count + 1,
        Name = safeName,
        Price = input.Price,
        Stock = input.Stock,
        Category = new ProductCategory { Id = 200, Name = "General Inventory" }
    };

    mockProductDatabase.Add(newItem);
    cache.Remove("CatalogList"); // Invalidate cache state upon modification

    return Results.Created($"/api/products/{newItem.Id}", newItem);
});

app.Run();

public class ProductItem
{
    public int Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public double Price { get; set; }
    public int Stock { get; set; }
    public ProductCategory? Category { get; set; }
}

public class ProductCategory
{
    public int Id { get; set; }
    public string Name { get; set; } = string.Empty;
}

public class ProductInput
{
    public string Name { get; set; } = string.Empty;
    public double Price { get; set; }
    public int Stock { get; set; }
    public string UserToken { get; set; } = string.Empty;
    public string UserRole { get; set; } = string.Empty;
}
