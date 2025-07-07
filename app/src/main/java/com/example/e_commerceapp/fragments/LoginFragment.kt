package com.example.e_commerceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.e_commerceapp.databinding.FragmentLoginBinding
import com.example.e_commerceapp.viewmodel.LoginViewModel

class LoginFragment: Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            buttonLogin.setOnClickListener {
                val email: String = edEmailLogin.text.toString()
                val password: String = edPasswordLogin.text.toString()

                viewModel.loginUsingEmailAndPassword(email, password)
            }
        }
    }
}