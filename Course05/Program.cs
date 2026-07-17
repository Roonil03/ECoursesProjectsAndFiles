using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System.Collections.Generic;
using System.Linq;
using UserManagementAPI.Models;
using UserManagementAPI.Middleware;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddEndpointsApiExplorer();

var usersDb = new List<User>
{
    new User { Id = 1, Name = "Alice Vance", Email = "alice@techhive.com" },
    new User { Id = 2, Name = "Bob Miller", Email = "bob@techhive.com" }
};

var app = builder.Build();

app.UseMiddleware<ErrorHandlingMiddleware>();
app.UseMiddleware<AuthenticationMiddleware>();
app.UseMiddleware<LoggingMiddleware>();

app.MapGet("/api/users", () => Results.Ok(usersDb));

app.MapGet("/api/users/{id:int}", (int id) =>
{
    var user = usersDb.FirstOrDefault(u => u.Id == id);
    return user is null ? Results.NotFound(new { error = $"User with ID {id} not found." }) : Results.Ok(user);
});

app.MapPost("/api/users", (User newUser) =>
{
    if (string.IsNullOrWhiteSpace(newUser.Name) || !newUser.Email.Contains("@"))
    {
        return Results.BadRequest(new { error = "Validation failed. Check name and email criteria." });
    }

    newUser.Id = usersDb.Count > 0 ? usersDb.Max(u => u.Id) + 1 : 1;
    usersDb.Add(newUser);
    return Results.Created($"/api/users/{newUser.Id}", newUser);
});

app.MapPut("/api/users/{id:int}", (int id, User updatedUser) =>
{
    var existingUser = usersDb.FirstOrDefault(u => u.Id == id);
    if (existingUser is null)
    {
        return Results.NotFound(new { error = $"User with ID {id} not found." });
    }

    if (string.IsNullOrWhiteSpace(updatedUser.Name) || !updatedUser.Email.Contains("@"))
    {
        return Results.BadRequest(new { error = "Validation errors encountered during data processing." });
    }

    existingUser.Name = updatedUser.Name;
    existingUser.Email = updatedUser.Email;
    return Results.Ok(existingUser);
});

app.MapDelete("/api/users/{id:int}", (int id) =>
{
    var user = usersDb.FirstOrDefault(u => u.Id == id);
    if (user is null)
    {
        return Results.NotFound(new { error = $"User with ID {id} not found." });
    }

    usersDb.Remove(user);
    return Results.NoContent();
});

app.Run();
