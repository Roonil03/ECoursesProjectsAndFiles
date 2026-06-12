package pkg

import (
	"fmt"
	"log"
	"os"

	"ecommerce-backend/internal/models"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var DB *gorm.DB

func ConnectDB() {
	host := os.Getenv("DB_HOST")
	if host == "" {
		host = "localhost"
	}
	user := os.Getenv("DB_USER")
	if user == "" {
		user = "ecommerce_user"
	}
	password := os.Getenv("DB_PASSWORD")
	if password == "" {
		password = "ecommerce_password"
	}
	dbname := os.Getenv("DB_NAME")
	if dbname == "" {
		dbname = "ecommerce_db"
	}
	port := os.Getenv("DB_PORT")
	if port == "" {
		port = "5432"
	}

	dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		host, user, password, dbname, port)

	var err error
	DB, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}

	err = DB.AutoMigrate(&models.User{}, &models.Product{}, &models.Cart{}, &models.Address{}, &models.Order{})
	if err != nil {
		log.Fatalf("Failed to migrate database: %v", err)
	}
	log.Println("Database connected and migrated successfully")
}
