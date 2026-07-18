using System;
using System.Collections.Generic;
using System.Security.Claims;
using System.Text.Json;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Caching.Memory;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddCors();
builder.Services.AddMemoryCache();

var app = builder.Build();

app.UseCors(policy =>
    policy.AllowAnyOrigin()
          .AllowAnyMethod()
          .AllowAnyHeader());

// Mock Data In-Memory Storage Context
var mockPortfolioDb = new DeveloperProfile
{
    FullName = "Alex Mercer",
    Bio = "Full-stack software engineer specializing in cloud architectures.",
    Skills = new List<string> { "C#", "ASP.NET Core", "Blazor WASM", "Docker" }
};

// Route 1: Authentication Simulation endpoint returning mock security context
app.MapPost("/api/auth/login", (LoginModel login) =>
{
    if (string.IsNullOrEmpty(login.Email) || string.IsNullOrEmpty(login.Password))
    {
        return Results.BadRequest("Missing required credentials.");
    }

    if (login.Email == "admin@skillsnap.com" && login.Password == "SecurePass123!")
    {
        return Results.Ok(new AuthResponse { Token = "mock-jwt-token-xyz", Role = "Admin" });
    }

    return Results.Unauthorized();
});

// Route 2: Fetch portfolio updates utilizing IMemoryCache optimizations
app.MapGet("/api/portfolio", (IMemoryCache cache) =>
{
    const string CacheKey = "DeveloperPortfolioData";

    if (!cache.TryGetValue(CacheKey, out DeveloperProfile? profile))
    {
        profile = mockPortfolioDb;
        var cacheOptions = new MemoryCacheEntryOptions()
            .SetAbsoluteExpiration(TimeSpan.FromMinutes(5))
            .SetSlidingExpiration(TimeSpan.FromMinutes(1));
        
        cache.Set(CacheKey, profile, cacheOptions);
    }

    return Results.Ok(profile);
});

// Route 3: Secure mutations updating data state fields
app.MapPost("/api/portfolio", (DeveloperProfile updatedProfile, IMemoryCache cache) =>
{
    if (string.IsNullOrWhiteSpace(updatedProfile.FullName) || updatedProfile.FullName.Length > 50)
    {
        return Results.BadRequest("Invalid validation range for user name metadata.");
    }

    mockPortfolioDb.FullName = updatedProfile.FullName;
    mockPortfolioDb.Bio = updatedProfile.Bio;
    mockPortfolioDb.Skills = updatedProfile.Skills;

    cache.Remove("DeveloperPortfolioData"); // Invalidate cache immediately

    return Results.Ok(mockPortfolioDb);
});

app.Run();

public class LoginModel { public string Email { get; set; } = string.Empty; public string Password { get; set; } = string.Empty; }
public class AuthResponse { public string Token { get; set; } = string.Empty; public string Role { get; set; } = string.Empty; }
public class DeveloperProfile
{
    public string FullName { get; set; } = string.Empty;
    public string Bio { get; set; } = string.Empty;
    public List<string> Skills { get; set; } = new();
}
