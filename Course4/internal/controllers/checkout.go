package controllers

import (
	"encoding/json"
	"net/http"

	"ecommerce-backend/internal/models"
	"ecommerce-backend/pkg"
	"github.com/gin-gonic/gin"
)

type CheckoutInput struct {
	AddressID    uint   `json:"address_id"`
	AddressValue string `json:"address_value"`
}

func Checkout(c *gin.Context) {
	userID := c.MustGet("user_id").(uint)
	var input CheckoutInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	if input.AddressID == 0 && input.AddressValue == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Address ID or Address Value is required"})
		return
	}

	tx := pkg.DB.Begin()

	var address models.Address
	if input.AddressID != 0 {
		if err := tx.Where("address_id = ? AND user_id = ?", input.AddressID, userID).First(&address).Error; err != nil {
			tx.Rollback()
			c.JSON(http.StatusNotFound, gin.H{"error": "Address not found"})
			return
		}
	} else {
		address = models.Address{
			UserID:       userID,
			AddressValue: input.AddressValue,
		}
		if err := tx.Create(&address).Error; err != nil {
			tx.Rollback()
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to create address"})
			return
		}
	}

	var cartItems []models.Cart
	if err := tx.Preload("Product").Where("user_id = ?", userID).Find(&cartItems).Error; err != nil || len(cartItems) == 0 {
		tx.Rollback()
		c.JSON(http.StatusBadRequest, gin.H{"error": "Cart is empty"})
		return
	}

	var orderValue float64
	for _, item := range cartItems {
		orderValue += float64(item.Quantity) * item.Product.Price
	}

	var user models.User
	if err := tx.First(&user, userID).Error; err != nil {
		tx.Rollback()
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to fetch user data"})
		return
	}

	if user.WalletBalance < orderValue {
		tx.Rollback()
		c.JSON(http.StatusBadRequest, gin.H{"error": "Insufficient Balance"})
		return
	}

	user.WalletBalance -= orderValue
	if err := tx.Save(&user).Error; err != nil {
		tx.Rollback()
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to update wallet balance"})
		return
	}

	orderItemsJSON, _ := json.Marshal(cartItems)

	order := models.Order{
		UserID:      userID,
		OrderValue:  orderValue,
		OrderItems:  orderItemsJSON,
		AddressID:   address.AddressID,
		OrderStatus: "SUCCESS",
	}

	if err := tx.Create(&order).Error; err != nil {
		tx.Rollback()
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to create order"})
		return
	}

	if err := tx.Where("user_id = ?", userID).Delete(&models.Cart{}).Error; err != nil {
		tx.Rollback()
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to clear cart"})
		return
	}

	tx.Commit()

	c.JSON(http.StatusOK, gin.H{"message": "Checkout successful", "order": order})
}
