package com.example.attend.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.attend.LecturerActivity
import com.example.attend.LoggedOutGraphDirections
import com.example.attend.MainActivity

import com.example.attend.R
import com.example.attend.databinding.FragmentLoginBinding
import com.example.attend.utils.onTextChanged
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class LoginFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val loginViewModel: LoginViewModel by stateViewModel(bundle = Bundle())
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.spinner.onItemSelectedListener = this
        return binding.root
    }

    private fun setupObservers() {
        loginViewModel.canNavigate.observe(viewLifecycleOwner, Observer {
            activity?.startActivity(Intent(context, LecturerActivity::class.java))
        })

        loginViewModel.errorEmitter.observe(viewLifecycleOwner, Observer{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.user_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        binding.username.editText?.setText(loginViewModel.username.value)
        binding.password.editText?.setText(loginViewModel.password.value)

        binding.username.editText?.onTextChanged { username -> loginViewModel.username.value = username }
        binding.password.editText?.onTextChanged { password -> loginViewModel.password.value = password }
        binding.login.setOnClickListener {
            loginViewModel.onLoginClicked()
        }
//        buttonRegister.onClick { viewModel.onRegisterClicked() }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Nothing for now
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val type = parent?.getItemAtPosition(position).toString()
        loginViewModel.userType.value = type
        loginViewModel.userId.value = "1"
        Timber.d("userType: $type")
    }

}