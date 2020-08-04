package com.example.attend.ui.login

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.attend.LoggedOutGraphDirections

import com.example.attend.R
import com.example.attend.databinding.FragmentLoginBinding
import com.example.attend.utils.onTextChanged
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by stateViewModel(bundle = Bundle())
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupObservers() {
        loginViewModel.canNavigate.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(LoggedOutGraphDirections.loggedOutToLoggedIn(it))
        })

        loginViewModel.errorEmitter.observe(viewLifecycleOwner, Observer{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        binding.username.editText?.setText(loginViewModel.username.value)
        binding.password.editText?.setText(loginViewModel.password.value)

        binding.username.editText?.onTextChanged { username -> loginViewModel.username.value = username }
        binding.password.editText?.onTextChanged { password -> loginViewModel.password.value = password }
        binding.login.setOnClickListener { loginViewModel.onLoginClicked() }
//        buttonRegister.onClick { viewModel.onRegisterClicked() }

    }

}