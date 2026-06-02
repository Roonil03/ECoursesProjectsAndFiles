package cms

import (
	"fmt"
	"net/http"
	"reflect"
	"strings"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
)

// Registry maps route names to reflect.Type and an in-memory store
type Registry struct {
	types map[string]reflect.Type
	store map[string]map[string]any
	mu    sync.RWMutex
}

// NewRegistry creates a new CMS registry
func NewRegistry() *Registry {
	return &Registry{
		types: make(map[string]reflect.Type),
		store: make(map[string]map[string]any),
	}
}

// RegisterModel takes a struct instance and registers it in the CMS
func (r *Registry) RegisterModel(model any) {
	t := reflect.TypeOf(model)
	if t.Kind() == reflect.Ptr {
		t = t.Elem()
	}

	if t.Kind() != reflect.Struct {
		panic("CMS models must be structs")
	}

	// Generate a route name from the struct name (lowercase + "s")
	routeName := strings.ToLower(t.Name()) + "s"

	r.mu.Lock()
	defer r.mu.Unlock()
	r.types[routeName] = t
	r.store[routeName] = make(map[string]any)

	fmt.Printf("Registered model: %s -> /api/%s\n", t.Name(), routeName)
}

// GenerateRoutes sets up CRUD routes for all registered models on the given gin router
func (r *Registry) GenerateRoutes(router *gin.Engine) {
	api := router.Group("/api")

	for routeName := range r.types {
		// Capture loop variable
		modelName := routeName
		modelType := r.types[modelName]

		group := api.Group("/" + modelName)
		{
			// GET /api/{type} - List all
			group.GET("", func(c *gin.Context) {
				r.mu.RLock()
				defer r.mu.RUnlock()

				items := r.store[modelName]
				var result []any
				for _, item := range items {
					result = append(result, item)
				}
				
				// Handle empty list gracefully
				if result == nil {
					result = make([]any, 0)
				}

				c.JSON(http.StatusOK, result)
			})

			// GET /api/{type}/:id - Get one
			group.GET("/:id", func(c *gin.Context) {
				id := c.Param("id")

				r.mu.RLock()
				defer r.mu.RUnlock()

				item, exists := r.store[modelName][id]
				if !exists {
					c.JSON(http.StatusNotFound, gin.H{"error": "Item not found"})
					return
				}
				c.JSON(http.StatusOK, item)
			})

			// POST /api/{type} - Create
			group.POST("", func(c *gin.Context) {
				// Create a new instance of the registered type dynamically using reflection
				newItemPtr := reflect.New(modelType)
				newItem := newItemPtr.Interface()

				if err := c.ShouldBindJSON(newItem); err != nil {
					c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
					return
				}

				// Basic ID generation for demonstration
				id := fmt.Sprintf("%d", time.Now().UnixNano())

				// Use reflection to set ID if an ID field exists
				idField := newItemPtr.Elem().FieldByName("ID")
				if idField.IsValid() && idField.CanSet() && idField.Kind() == reflect.String {
					idField.SetString(id)
				}

				r.mu.Lock()
				defer r.mu.Unlock()
				r.store[modelName][id] = newItemPtr.Elem().Interface()

				c.JSON(http.StatusCreated, gin.H{
					"message": "Created successfully",
					"id":      id,
					"data":    r.store[modelName][id],
				})
			})

			// PUT /api/{type}/:id - Update
			group.PUT("/:id", func(c *gin.Context) {
				id := c.Param("id")

				r.mu.Lock()
				defer r.mu.Unlock()

				_, exists := r.store[modelName][id]
				if !exists {
					c.JSON(http.StatusNotFound, gin.H{"error": "Item not found"})
					return
				}

				// Create a new instance dynamically
				updatedItemPtr := reflect.New(modelType)
				updatedItem := updatedItemPtr.Interface()

				if err := c.ShouldBindJSON(updatedItem); err != nil {
					c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
					return
				}

				// Ensure ID isn't modified
				idField := updatedItemPtr.Elem().FieldByName("ID")
				if idField.IsValid() && idField.CanSet() && idField.Kind() == reflect.String {
					idField.SetString(id)
				}

				r.store[modelName][id] = updatedItemPtr.Elem().Interface()
				c.JSON(http.StatusOK, gin.H{"message": "Updated successfully", "data": r.store[modelName][id]})
			})

			// DELETE /api/{type}/:id - Delete
			group.DELETE("/:id", func(c *gin.Context) {
				id := c.Param("id")

				r.mu.Lock()
				defer r.mu.Unlock()

				if _, exists := r.store[modelName][id]; !exists {
					c.JSON(http.StatusNotFound, gin.H{"error": "Item not found"})
					return
				}

				delete(r.store[modelName], id)
				c.JSON(http.StatusOK, gin.H{"message": "Deleted successfully"})
			})
		}
	}
}
