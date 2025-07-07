package com.example.e_commerceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.util.RegisterFieldsValidation
import com.example.e_commerceapp.util.RegisterValidation
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.validateEmail
import com.example.e_commerceapp.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Initial())
    val register : Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldsValidation>()
    val validation: Flow<RegisterFieldsValidation> = _validation.receiveAsFlow()

     fun createAccountUsingEmailAndPassword(user: User, password: String) {

        if(checkValidation(user, password)) {
            _register.value = Resource.Loading()
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { result ->
                    result.user?.let {
                        _register.value = Resource.Success(it)
                        Log.d("register", "succesful")
                    }
                }.addOnFailureListener { error ->
                    _register.value = Resource.Error(error.message.toString())
                    Log.d("register", "failure: ${error.message.toString()}")
                }
        } else {
            val registerValidation: RegisterFieldsValidation = RegisterFieldsValidation(
                validateEmail(user.email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(registerValidation)
            }
        }
    }

    fun checkValidation(user: User, password: String): Boolean {
        val emailValidation: RegisterValidation = validateEmail(user.email)
        val passwordValidation: RegisterValidation = validatePassword(password)

        val shouldRegister: Boolean = emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
}