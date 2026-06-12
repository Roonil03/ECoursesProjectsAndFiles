package controllers

import (
	"net/http"
	"strconv"

	"ecommerce-backend/internal/models"
	"ecommerce-backend/pkg"
	"github.com/gin-gonic/gin"
)

type CartInput struct {
	ProductID uint `json:"product_id" binding:"required"`
	Quantity  int  `json:"quantity" binding:"required,min=1"`
}

func AddToCart(c *gin.Context) {
	userID := c.MustGet("user_id").(uint)
	var input CartInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	var product models.Product
	if err := pkg.DB.First(&product, input.ProductID).Error; err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Product not found"})
		return
	}

	if product.Stock < input.Quantity {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Insufficient stock"})
		return
	}

	var cartItem models.Cart
	if err := pkg.DB.Where("user_id = ? AND product_id = ?", userID, input.ProductID).First(&cartItem).Error; err != nil {
		cartItem = models.Cart{
			UserID:    userID,
			ProductID: input.ProductID,
			Quantity:  input.Quantity,
		}
		pkg.DB.Create(&cartItem)
	} else {
		cartItem.Quantity += input.Quantity
		pkg.DB.Save(&cartItem)
	}

	c.JSON(http.StatusOK, gin.H{"message": "Item added to cart", "cart": cartItem})
}

func GetCart(c *gin.Context) {
	userID := c.MustGet("user_id").(uint)

	var cartItems []models.Cart
	if err := pkg.DB.Preload("Product").Where("user_id = ?", userID).Find(&cartItems).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to fetch cart"})
		return
	}

	c.JSON(http.StatusOK, cartItems)
}

func RemoveFromCart(c *gin.Context) {
	userID := c.MustGet("user_id").(uint)
	productIDStr := c.Param("product_id")
	productID, err := strconv.ParseUint(productIDStr, 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid product ID"})
		return
	}

	if err := pkg.DB.Where("user_id = ? AND product_id = ?", userID, productID).Delete(&models.Cart{}).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to remove item from cart"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "Item removed from cart"})
}
