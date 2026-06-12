package main

import (
	"log"

	"ecommerce-backend/internal/routes"
	"ecommerce-backend/pkg"
)

func main() {
	pkg.ConnectDB()

	r := routes.SetupRouter()
	
	log.Println("Server running on :8080")
	if err := r.Run(":8080"); err != nil {
		log.Fatalf("Server failed: %v", err)
	}
}
