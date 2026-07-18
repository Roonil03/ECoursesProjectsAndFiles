// Tests/TestInputValidation.cs
using NUnit.Framework;
using SafeVault;

namespace SafeVault.Tests
{
    [TestFixture]
    public class TestInputValidation
    {
        private DatabaseRepository _repo;

        [SetUp]
        public void Setup()
        {
            _repo = new DatabaseRepository();
            _repo.ExecuteParameterizedInsert("valid_user", "test@safevault.com", "SecurePass123", "User");
            _repo.ExecuteParameterizedInsert("admin_user", "admin@safevault.com", "AdminPass123", "Admin");
        }

        [Test]
        public void TestForSQLInjection()
        {
            string maliciousInput = "' OR '1'='1";
            bool result = _repo.ExecuteParameterizedInsert(maliciousInput, "attacker@vault.com", "pass", "User");
            Assert.IsFalse(result);
            
            var user = _repo.GetUserSecurely(maliciousInput);
            Assert.IsNull(user);
        }

        [Test]
        public void TestForXSS()
        {
            string cleanText = SecurityService.SanitizeForXSS("<script>alert('hack')</script>");
            Assert.AreNotEqual("<script>alert('hack')</script>", cleanText);
            Assert.IsTrue(cleanText.Contains("&lt;script&gt;"));
        }

        [Test]
        public void TestAuthenticationSuccess()
        {
            var user = _repo.GetUserSecurely("valid_user");
            Assert.IsNotNull(user);
            bool isValid = SecurityService.VerifyPassword("SecurePass123", user.PasswordHash);
            Assert.IsTrue(isValid);
        }

        [Test]
        public void TestRoleBasedAuthorization()
        {
            var regularUser = _repo.GetUserSecurely("valid_user");
            var adminUser = _repo.GetUserSecurely("admin_user");

            Assert.IsFalse(AuthorizationManager.AccessAdminDashboard(regularUser!));
            Assert.IsTrue(AuthorizationManager.AccessAdminDashboard(adminUser!));
        }
    }
}
