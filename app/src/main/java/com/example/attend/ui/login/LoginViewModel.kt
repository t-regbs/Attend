package com.example.attend.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import com.example.attend.LoggedOutGraphDirections
import com.example.attend.data.LoginRepository
import com.example.attend.data.Result

import com.example.attend.R
import com.example.attend.app.AuthenticationManager

class LoginViewModel(
        private val savedStateHandle: SavedStateHandle,
        private val authenticationManager: AuthenticationManager
) : ViewModel() {

    val username: MutableLiveData<String> = savedStateHandle.getLiveData("username", "")
    val password: MutableLiveData<String> = savedStateHandle.getLiveData("password", "")

    val canNavigate = MutableLiveData<String>()
    val errorEmitter = MutableLiveData<String>()

    fun onLoginClicked() {
        if (username.value!!.isNotBlank() && password.value!!.isNotBlank()) {
            val username = username.value!!

            authenticationManager.saveRegistration(username)
            canNavigate.postValue(username)
        } else {
            errorEmitter.postValue("Invalid username or password!")
        }
    }
}