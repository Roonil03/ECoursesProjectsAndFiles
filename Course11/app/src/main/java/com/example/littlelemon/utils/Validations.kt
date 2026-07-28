package com.example.littlelemon.utils

fun validateEmail(email: String): Boolean {
    val emailRegex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$".toRegex()
    return emailRegex.matches(email)
}

fun validateName(name: String): Boolean {
    if (name.isBlank()) return false
    val nameRegex = "^[a-zA-Z\\s]+$".toRegex()
    return nameRegex.matches(name.trim())
}

fun validatePhone(phone: String): Boolean {
    val phoneRegex = "^[0-9+()\\-\\s]*$".toRegex()
    return phoneRegex.matches(phone)
}
