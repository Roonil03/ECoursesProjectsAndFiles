package routes

import (
	"ecommerce-backend/internal/controllers"
	"ecommerce-backend/internal/middleware"
	"github.com/gin-gonic/gin"
)

func SetupRouter() *gin.Engine {
	r := gin.Default()

	api := r.Group("/api")
	{
		api.POST("/register", controllers.Register)
		api.POST("/login", controllers.Login)
		api.GET("/products", controllers.GetProducts)

		protected := api.Group("/")
		protected.Use(middleware.AuthRequired())
		{
			protected.POST("/cart", controllers.AddToCart)
			protected.GET("/cart", controllers.GetCart)
			protected.DELETE("/cart/:product_id", controllers.RemoveFromCart)

			protected.POST("/checkout", controllers.Checkout)
		}
	}

	return r
}
