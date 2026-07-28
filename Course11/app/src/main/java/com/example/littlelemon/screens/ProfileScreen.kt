package com.example.littlelemon.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)

    var firstName by remember { mutableStateOf(sharedPreferences.getString("firstName", "") ?: "") }
    var lastName by remember { mutableStateOf(sharedPreferences.getString("lastName", "") ?: "") }
    var email by remember { mutableStateOf(sharedPreferences.getString("email", "") ?: "") }
    var phone by remember { mutableStateOf(sharedPreferences.getString("phone", "") ?: "") }

    var orderStatuses by remember { mutableStateOf(sharedPreferences.getBoolean("orderStatuses", true)) }
    var passwordChanges by remember { mutableStateOf(sharedPreferences.getBoolean("passwordChanges", true)) }
    var specialOffers by remember { mutableStateOf(sharedPreferences.getBoolean("specialOffers", true)) }
    var newsletter by remember { mutableStateOf(sharedPreferences.getBoolean("newsletter", true)) }

    fun handleSaveChanges() {
        sharedPreferences.edit()
            .putString("firstName", firstName.trim())
            .putString("lastName", lastName.trim())
            .putString("email", email.trim())
            .putString("phone", phone.trim())
            .putBoolean("orderStatuses", orderStatuses)
            .putBoolean("passwordChanges", passwordChanges)
            .putBoolean("specialOffers", specialOffers)
            .putBoolean("newsletter", newsletter)
            .apply()
        Toast.makeText(context, "Profile changes saved successfully.", Toast.LENGTH_SHORT).show()
    }

    fun handleDiscardChanges() {
        firstName = sharedPreferences.getString("firstName", "") ?: ""
        lastName = sharedPreferences.getString("lastName", "") ?: ""
        email = sharedPreferences.getString("email", "") ?: ""
        phone = sharedPreferences.getString("phone", "") ?: ""
        orderStatuses = sharedPreferences.getBoolean("orderStatuses", true)
        passwordChanges = sharedPreferences.getBoolean("passwordChanges", true)
        specialOffers = sharedPreferences.getBoolean("specialOffers", true)
        newsletter = sharedPreferences.getBoolean("newsletter", true)
    }

    fun handleLogout() {
        sharedPreferences.edit().clear().apply()
        navController.navigate("Onboarding") {
            popUpTo(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Personal information",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(text = "Avatar", fontSize = 14.sp, color = Color(0xFF333333), modifier = Modifier.padding(bottom = 8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /* Change Avatar logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF495E57)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Change", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = { /* Remove Avatar logic */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF495E57)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF495E57))
            ) {
                Text(text = "Remove", fontWeight = FontWeight.Bold)
            }
        }

        @Composable
        fun InputField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF333333))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
        }

        InputField("First Name", firstName, { firstName = it })
        InputField("Last Name", lastName, { lastName = it })
        InputField("Email", email, { email = it }, KeyboardType.Email)
        InputField("Phone number", phone, { phone = it }, KeyboardType.Phone)

        Text(
            text = "Email notifications",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        @Composable
        fun CheckboxRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF495E57))
                )
                Text(text = label, fontSize = 16.sp, color = Color(0xFF333333))
            }
        }

        CheckboxRow("Order statuses", orderStatuses) { orderStatuses = it }
        CheckboxRow("Password changes", passwordChanges) { passwordChanges = it }
        CheckboxRow("Special offers", specialOffers) { specialOffers = it }
        CheckboxRow("Newsletter", newsletter) { newsletter = it }

        Button(
            onClick = { handleLogout() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .height(48.dp)
        ) {
            Text(text = "Log out", color = Color(0xFF333333), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { handleDiscardChanges() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF495E57)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF495E57))
            ) {
                Text(text = "Discard changes", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { handleSaveChanges() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF495E57)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Save changes", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}
