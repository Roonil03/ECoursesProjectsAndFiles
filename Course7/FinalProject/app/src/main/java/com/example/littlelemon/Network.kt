package com.example.littlelemon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuNetwork(
    @SerialName("menu")
    val menu: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("price")
    val price: Double
) {
    fun toMenuItemRoom(): MenuItemRoom {
        val derivedCategory = when (id) {
            in 1..4 -> "Appetizers"
            in 5..8 -> "Salads"
            else -> "Beverages"
        }
        return MenuItemRoom(
            id = id,
            title = title,
            price = price,
            category = derivedCategory
        )
    }
}
