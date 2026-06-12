package tests

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"ecommerce-backend/internal/controllers"
	"ecommerce-backend/internal/models"
	"ecommerce-backend/pkg"
	"github.com/gin-gonic/gin"
	"github.com/glebarez/sqlite"
	"gorm.io/gorm"
)

func setupTestDB() {
	db, err := gorm.Open(sqlite.Open("file::memory:?cache=shared"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	db.AutoMigrate(&models.User{}, &models.Product{}, &models.Cart{}, &models.Address{}, &models.Order{})
	pkg.DB = db
}

func TestCheckoutInsufficientFunds(t *testing.T) {
	setupTestDB()

	user := models.User{
		Email:         "test@test.com",
		Password:      "hashed",
		WalletBalance: 50.00, // Insufficient balance
	}
	pkg.DB.Create(&user)

	product := models.Product{
		Name:     "Test Product",
		Category: "Test",
		Price:    100.00, // More than wallet balance
		Stock:    10,
	}
	pkg.DB.Create(&product)

	cartItem := models.Cart{
		UserID:    user.UserID,
		ProductID: product.ProductID,
		Quantity:  1,
	}
	pkg.DB.Create(&cartItem)

	gin.SetMode(gin.TestMode)
	r := gin.Default()
	r.POST("/checkout", func(c *gin.Context) {
		c.Set("user_id", user.UserID)
		controllers.Checkout(c)
	})

	input := controllers.CheckoutInput{
		AddressValue: "123 Test St",
	}
	body, _ := json.Marshal(input)

	req, _ := http.NewRequest(http.MethodPost, "/checkout", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	r.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}

	var balance float64
	pkg.DB.Model(&models.User{}).Where("user_id = ?", user.UserID).Select("wallet_balance").Scan(&balance)
	if balance != 50.00 {
		t.Errorf("Expected wallet balance to remain 50.00, got %f", balance)
	}
}
