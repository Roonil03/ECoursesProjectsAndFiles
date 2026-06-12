package controllers

import (
	"net/http"
	"strings"

	"ecommerce-backend/internal/models"
	"ecommerce-backend/pkg"
	"github.com/gin-gonic/gin"
)

func GetProducts(c *gin.Context) {
	name := c.Query("name")
	category := c.Query("category")
	priceGte := c.Query("price_gte")
	priceLte := c.Query("price_lte")
	sortBy := c.Query("sort_by")
	order := c.Query("order")

	query := pkg.DB.Model(&models.Product{})

	if name != "" {
		query = query.Where("name ILIKE ?", "%"+name+"%")
	}
	if category != "" {
		query = query.Where("category = ?", category)
	}
	if priceGte != "" {
		query = query.Where("price >= ?", priceGte)
	}
	if priceLte != "" {
		query = query.Where("price <= ?", priceLte)
	}

	if sortBy != "" {
		if strings.ToLower(order) == "desc" {
			query = query.Order(sortBy + " desc")
		} else {
			query = query.Order(sortBy + " asc")
		}
	}

	var products []models.Product
	if err := query.Find(&products).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to fetch products"})
		return
	}

	c.JSON(http.StatusOK, products)
}
