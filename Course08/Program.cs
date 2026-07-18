using System;
using System.Collections.Generic;
using System.Text.Encodings.Web;
using System.Text.RegularExpressions;
using System.Security.Cryptography;
using System.Text;

namespace SafeVault
{
    public class UserRecord
    {
        public string Username { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public string PasswordHash { get; set; } = string.Empty;
        public string Role { get; set; } = string.Empty;
    }

    public class SecurityService
    {
        public static string SanitizeForXSS(string input)
        {
            if (string.IsNullOrEmpty(input)) return string.Empty;
            return HtmlEncoder.Default.Encode(input);
        }

        public static bool ValidateInputs(string username, string email)
        {
            var usernameRegex = new Regex(@"^[a-zA-Z0-9_]{3,20}$");
            var emailRegex = new Regex(@"^[^@\s]+@[^@\s]+\.[^@\s]+$");
            return usernameRegex.IsMatch(username) && emailRegex.IsMatch(email);
        }

        public static string HashPassword(string password)
        {
            using var sha256 = SHA256.Create();
            var bytes = sha256.ComputeHash(Encoding.UTF8.GetBytes(password));
            return Convert.ToBase64String(bytes);
        }

        public static bool VerifyPassword(string password, string storedHash)
        {
            return HashPassword(password) == storedHash;
        }
    }

    public class DatabaseRepository
    {
        private readonly List<UserRecord> _mockDb = new();

        public bool ExecuteParameterizedInsert(string username, string email, string password, string role)
        {
            if (!SecurityService.ValidateInputs(username, email))
            {
                return false;
            }

            var newUser = new UserRecord
            {
                Username = SecurityService.SanitizeForXSS(username),
                Email = SecurityService.SanitizeForXSS(email),
                PasswordHash = SecurityService.HashPassword(password),
                Role = role
            };

            _mockDb.Add(newUser);
            return true;
        }

        public UserRecord? GetUserSecurely(string username)
        {
            return _mockDb.Find(u => u.Username == username);
        }
    }

    public class AuthorizationManager
    {
        public static bool AccessAdminDashboard(UserRecord user)
        {
            return user != null && user.Role.Equals("Admin", StringComparison.OrdinalIgnoreCase);
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("SafeVault Application Tier Initialized Successfully.");
        }
    }
}
