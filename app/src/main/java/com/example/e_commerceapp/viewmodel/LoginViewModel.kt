package com.example.e_commerceapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth): ViewModel() {

    private val _login = MutableStateFlow<Resource<FirebaseUser>>(Resource.Initial())
    var login: Flow<Resource<FirebaseUser>> = _login

    fun loginUsingEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let { user ->
                    _login.value = Resource.Success(user)
                    Log.d("login", "Success")
                }
            }
            .addOnFailureListener { error ->
                error.message?.let { msg ->
                    _login.value = Resource.Error(msg)
                    Log.d("login", "Error: $msg")
                }
            }
    }
}