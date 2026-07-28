package com.example.littlelemon.navigators

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.screens.HomeScreen
import com.example.littlelemon.screens.OnboardingScreen
import com.example.littlelemon.screens.ProfileScreen
import com.example.littlelemon.screens.SubscribeScreen
import com.example.littlelemon.screens.WelcomeScreen

@Composable
fun RootNavigator() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)
    val isOnboardingCompleted = sharedPreferences.getBoolean("isOnboardingCompleted", false)
    
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = if (isOnboardingCompleted) "Home" else "Onboarding"
    ) {
        composable("Welcome") {
            WelcomeScreen(navController)
        }
        composable("Subscribe") {
            SubscribeScreen(navController)
        }
        composable("Onboarding") {
            OnboardingScreen(navController)
        }
        composable("Home") {
            HomeScreen(navController)
        }
        composable("Profile") {
            ProfileScreen(navController)
        }
    }
}
