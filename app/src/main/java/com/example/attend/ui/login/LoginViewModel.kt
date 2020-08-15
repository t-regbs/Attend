package com.example.attend.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
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
    val userType: MutableLiveData<String> = savedStateHandle.getLiveData("userType", "")
    val userId: MutableLiveData<String> = savedStateHandle.getLiveData("userId", "")

    val canNavigate = MutableLiveData<String>()
    val errorEmitter = MutableLiveData<String>()

    fun onLoginClicked() {
        if (username.value!!.isNotBlank() && password.value!!.isNotBlank()) {
            val user = username.value!!
            val userType = userType.value!!
            val userId = userId.value!!

            authenticationManager.saveRegistration(user)
            authenticationManager.saveUserType(userType)
            authenticationManager.saveUserId(userId)
            canNavigate.postValue(userType)
        } else {
            errorEmitter.postValue("Invalid username or password!")
        }
    }
}