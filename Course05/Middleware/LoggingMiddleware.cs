using Microsoft.AspNetCore.Http;
using System;
using System.Threading.Tasks;

namespace UserManagementAPI.Middleware
{
    public class LoggingMiddleware
    {
        private readonly RequestDelegate _next;

        public LoggingMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            await _next(context);

            Console.WriteLine($"[AUDIT LOG] Method: {context.Request.Method} | Path: {context.Request.Path} | Status Code: {context.Response.StatusCode}");
        }
    }
}
