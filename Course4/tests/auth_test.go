package tests

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"ecommerce-backend/internal/middleware"
	"ecommerce-backend/pkg"
	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
)

func TestPasswordHashing(t *testing.T) {
	password := "mysecurepassword"
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		t.Fatalf("Failed to hash password: %v", err)
	}

	err = bcrypt.CompareHashAndPassword(hashedPassword, []byte(password))
	if err != nil {
		t.Errorf("Password hash does not match original password")
	}

	err = bcrypt.CompareHashAndPassword(hashedPassword, []byte("wrongpassword"))
	if err == nil {
		t.Errorf("Password hash matches incorrect password")
	}
}

func TestJWTGenerationAndValidation(t *testing.T) {
	userID := uint(1)
	token, err := pkg.GenerateToken(userID)
	if err != nil {
		t.Fatalf("Failed to generate token: %v", err)
	}

	claims, err := pkg.ValidateToken(token)
	if err != nil {
		t.Fatalf("Failed to validate token: %v", err)
	}

	if claims.UserID != userID {
		t.Errorf("Expected UserID %d, got %d", userID, claims.UserID)
	}
}

func TestAuthMiddlewareUnauthorized(t *testing.T) {
	gin.SetMode(gin.TestMode)
	r := gin.Default()
	r.GET("/protected", middleware.AuthRequired(), func(c *gin.Context) {
		c.Status(http.StatusOK)
	})

	req, _ := http.NewRequest(http.MethodGet, "/protected", nil)
	w := httptest.NewRecorder()
	r.ServeHTTP(w, req)

	if w.Code != http.StatusUnauthorized {
		t.Errorf("Expected status %d, got %d", http.StatusUnauthorized, w.Code)
	}
}
