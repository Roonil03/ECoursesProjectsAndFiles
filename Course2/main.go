package main

import (
	"dynamic-cms/cms"
	"log"

	"github.com/gin-gonic/gin"
)

// Define some arbitrary structs to act as dynamic content types
// Tags are used by Gin's json binder
type Article struct {
	ID      string `json:"id"`
	Title   string `json:"title" binding:"required"`
	Content string `json:"content"`
	Author  string `json:"author"`
}

type Event struct {
	ID       string `json:"id"`
	Name     string `json:"name" binding:"required"`
	Location string `json:"location"`
	Date     string `json:"date"`
}

type BlogPost struct {
	ID        string   `json:"id"`
	Headline  string   `json:"headline" binding:"required"`
	Tags      []string `json:"tags"`
	Published bool     `json:"published"`
}

func main() {
	// 1. Initialize the CMS Registry
	registry := cms.NewRegistry()

	// 2. Register models. The CMS uses reflection to discover their shape
	registry.RegisterModel(Article{})
	registry.RegisterModel(Event{})
	registry.RegisterModel(BlogPost{})

	// 3. Initialize Gin router
	r := gin.Default()

	// 4. Generate dynamic CRUD routes based on registered models
	registry.GenerateRoutes(r)

	log.Println("Server starting on :8080...")
	log.Println("Available dynamic endpoints:")
	log.Println("  /api/articles")
	log.Println("  /api/events")
	log.Println("  /api/blogposts")

	r.Run(":8080")
}
