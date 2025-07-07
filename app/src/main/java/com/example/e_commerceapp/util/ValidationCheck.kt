package com.example.e_commerceapp.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if(email.isEmpty()) {
        return RegisterValidation.Failure(message = "Please enter your email")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return RegisterValidation.Failure(message = "Please enter a valid email")
    }

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if(password.isEmpty()) {
        return RegisterValidation.Failure(message = "Please enter your password")
    }
    if(password.length < 6) {
        return RegisterValidation.Failure(message = "Password cannot be less than 6 characters")
    }

    return RegisterValidation.Success
}