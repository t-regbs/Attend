package com.example.attend.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.attend.LecturerActivity
import com.example.attend.MainActivity
import com.example.attend.R
import com.example.attend.databinding.FragmentLoginBinding
import com.example.attend.utils.onTextChanged
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

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
            if (it == "Admin") {
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                startActivity(Intent(context, LecturerActivity::class.java))
            }
        })

        loginViewModel.errorEmitter.observe(viewLifecycleOwner, Observer{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        val type = arrayOf("Admin", "Lecturer")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_simple_spinner_item,
            type
        )
        binding.outlinedExposedDropdown.setAdapter(adapter)

        binding.username.editText?.setText(loginViewModel.username.value)
        binding.password.editText?.setText(loginViewModel.password.value)

        binding.username.editText?.onTextChanged { username -> loginViewModel.username.value = username }
        binding.password.editText?.onTextChanged { password -> loginViewModel.password.value = password }
        binding.outlinedExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val type = parent?.getItemAtPosition(position).toString()
                loginViewModel.userType.value = type
                loginViewModel.userId.value = "1"
                Timber.d("userType: $type")
            }
        binding.login.setOnClickListener {
            loginViewModel.onLoginClicked()
        }
//        buttonRegister.onClick { viewModel.onRegisterClicked() }

    }
}