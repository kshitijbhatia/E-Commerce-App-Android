package com.example.e_commerceapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.R
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.databinding.FragmentRegisterBinding
import com.example.e_commerceapp.util.RegisterFieldsValidation
import com.example.e_commerceapp.util.RegisterValidation
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.concurrent.thread

private const val TAG: String = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegister.setOnClickListener {
                val user: User = User(
                    firstName = edFirstName.text.toString().trim(),
                    lastName = edLastName.text.toString().trim(),
                    email = edEmailRegister.text.toString().trim(),
                )
                val password: String = edPasswordRegister.text.toString()

                viewModel.createAccountUsingEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.register.collect {
                when(it) {
                    is Resource.Loading -> {
                        // binding.buttonRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.message.toString())
                        // binding.buttonRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        // binding.buttonRegister.revertAnimation()
                    }
                    is Resource.Initial -> {

                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect { validation ->
                if(validation.email is RegisterValidation.Failure) {
                    binding.edEmailRegister.apply {
                        requestFocus()
                        error = validation.email.message
                    }
                }
                if(validation.password is RegisterValidation.Failure) {
                    binding.edPasswordRegister.apply {
                        requestFocus()
                        error = validation.password.message
                    }
                }
            }
        }
    }
}

suspend fun getMessage1(): String {
    delay(1000)
    Log.d("print", "inside getMessage1")
    return "Hello"
}

suspend fun getMessage2(): String {
    delay(1000)
    Log.d("print", "inside getMessage2")
    return "World!"
}