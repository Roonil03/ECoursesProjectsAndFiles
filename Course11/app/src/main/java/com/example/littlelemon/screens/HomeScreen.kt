package com.example.littlelemon.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.R

data class MenuItem(
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    val image: Int,
    val category: String
)

val INITIAL_MENU_DATA = listOf(
    MenuItem(1, "Greek Salad", "$12.99", "Our delicious salad is served with Feta cheese and peeled cucumber. Includes tomatoes, onions, olives, and extra virgin olive oil.", R.drawable.splash, "starters"),
    MenuItem(2, "Bruschetta", "$7.99", "Delicious grilled bread topped with garlic, tomatoes, olive oil and fresh basil.", R.drawable.splash, "starters"),
    MenuItem(3, "Grilled Fish", "$20.00", "Our tender grilled fish seasoned with fresh herbs and lemon zest, served with a side of roasted vegetables.", R.drawable.splash, "mains"),
    MenuItem(4, "Pasta Carbonara", "$14.99", "Classic Italian pasta with creamy egg sauce, pecorino cheese, crisp pancetta, and black pepper.", R.drawable.splash, "mains"),
    MenuItem(5, "Lemon Dessert", "$6.99", "Traditional homemade lemon tart with a buttery shortbread crust and delicate meringue topping.", R.drawable.splash, "desserts"),
    MenuItem(6, "Baklava", "$5.99", "Rich, sweet pastry made of layers of filo filled with chopped nuts and sweetened with honey syrup.", R.drawable.splash, "desserts"),
    MenuItem(7, "House Wine", "$8.00", "A glass of our signature house red or white wine sourced from Mediterranean vineyards.", R.drawable.splash, "drinks"),
    MenuItem(8, "Fresh Lemonade", "$4.50", "Refreshing squeezed lemonade made with mint leaves and a touch of organic honey.", R.drawable.splash, "drinks")
)

val CATEGORIES = listOf("Starters", "Mains", "Desserts", "Drinks")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(emptySet<String>()) }

    fun handleCategoryPress(category: String) {
        val lower = category.lowercase()
        selectedCategories = if (selectedCategories.contains(lower)) {
            selectedCategories - lower
        } else {
            selectedCategories + lower
        }
    }

    val filteredMenu = INITIAL_MENU_DATA.filter { item ->
        val matchesSearch = searchQuery.isBlank() || item.name.contains(searchQuery, ignoreCase = true) || item.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategories.isEmpty() || selectedCategories.contains(item.category.lowercase())
        matchesSearch && matchesCategory
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(44.dp))
            Image(
                painter = painterResource(id = R.drawable.little_lemon_logo),
                contentDescription = "Logo",
                modifier = Modifier.height(40.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable { navController.navigate("Profile") }
            )
        }

        // Hero Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF495E57))
                .padding(16.dp)
        ) {
            Text(text = "Little Lemon", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF4CE14))
            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                    Text(text = "Chicago", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(
                        text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                        fontSize = 16.sp, color = Color(0xFFEDEFEE), lineHeight = 22.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.splash),
                    contentDescription = "Hero Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp))
                )
            }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search our delicious menu...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth().background(Color(0xFFEDEFEE), RoundedCornerShape(8.dp)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        // Breakdown Section
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ORDER FOR DELIVERY!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333), modifier = Modifier.padding(bottom = 12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CATEGORIES.forEach { category ->
                    val isSelected = selectedCategories.contains(category.lowercase())
                    Button(
                        onClick = { handleCategoryPress(category) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF495E57) else Color(0xFFEDEFEE),
                            contentColor = if (isSelected) Color(0xFFEDEFEE) else Color(0xFF495E57)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(text = category, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Divider(color = Color(0xFFEDEFEE), thickness = 1.dp)

        // Menu List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(filteredMenu) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                        Text(text = item.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333), modifier = Modifier.padding(bottom = 4.dp))
                        Text(text = item.description, fontSize = 14.sp, color = Color(0xFF495E57), modifier = Modifier.padding(bottom = 8.dp), maxLines = 2)
                        Text(text = item.price, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF495E57))
                    }
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
                    )
                }
                Divider(color = Color(0xFFEDEFEE), thickness = 1.dp)
            }
        }
    }
}
