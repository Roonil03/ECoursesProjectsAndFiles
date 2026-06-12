package models

import (
	"encoding/json"
	"time"
)

type User struct {
	UserID        uint      `gorm:"primaryKey;autoIncrement" json:"user_id"`
	Email         string    `gorm:"uniqueIndex;not null" json:"email"`
	Password      string    `gorm:"not null" json:"-"`
	WalletBalance float64   `gorm:"type:numeric(10,2);default:1000.00" json:"wallet_balance"`
	Addresses     []Address `gorm:"foreignKey:UserID" json:"addresses,omitempty"`
}

type Product struct {
	ProductID uint    `gorm:"primaryKey;autoIncrement" json:"product_id"`
	Name      string  `gorm:"not null" json:"name"`
	Category  string  `gorm:"not null" json:"category"`
	Price     float64 `gorm:"type:numeric(10,2);not null" json:"price"`
	Stock     int     `gorm:"not null" json:"stock"`
}

type Cart struct {
	UserID    uint    `gorm:"primaryKey;autoIncrement:false" json:"user_id"`
	ProductID uint    `gorm:"primaryKey;autoIncrement:false" json:"product_id"`
	Quantity  int     `gorm:"not null" json:"quantity"`
	User      User    `gorm:"foreignKey:UserID" json:"-"`
	Product   Product `gorm:"foreignKey:ProductID" json:"product"`
}

type Address struct {
	AddressID    uint   `gorm:"primaryKey;autoIncrement" json:"address_id"`
	UserID       uint   `gorm:"not null" json:"user_id"`
	AddressValue string `gorm:"not null" json:"address_value"`
}

type Order struct {
	OrderID     uint            `gorm:"primaryKey;autoIncrement" json:"order_id"`
	UserID      uint            `gorm:"not null" json:"user_id"`
	OrderValue  float64         `gorm:"type:numeric(10,2);not null" json:"order_value"`
	OrderItems  json.RawMessage `gorm:"type:jsonb;not null" json:"order_items"`
	OrderDate   time.Time       `gorm:"autoCreateTime" json:"order_date"`
	AddressID   uint            `gorm:"not null" json:"address_id"`
	OrderStatus string          `gorm:"not null;default:'PENDING'" json:"order_status"`

	User    User    `gorm:"foreignKey:UserID" json:"-"`
	Address Address `gorm:"foreignKey:AddressID" json:"-"`
}
