package com.example.e_commerceapp.util

sealed class RegisterValidation() {
    object Success: RegisterValidation()
    data class Failure(val message: String): RegisterValidation()
}

data class RegisterFieldsValidation(
    val email: RegisterValidation,
    val password: RegisterValidation
)