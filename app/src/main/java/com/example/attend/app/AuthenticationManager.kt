package com.example.attend.app

import android.content.SharedPreferences

class AuthenticationManager(val sharedPref: SharedPreferences) {

    fun isAuthenticated(): Boolean =
        sharedPref.getString("username", "")!!.isNotEmpty()

    fun saveRegistration(username: String) {
        sharedPref.edit().putString("username", username).apply()
    }

    fun clearRegistration() {
        sharedPref.edit().remove("username").apply()
    }

    fun getAuthenticatedUser(): String {
        return checkNotNull(sharedPref.getString("username", "").takeIf { it!!.isNotEmpty() })
    }

    var authToken: String = "" // why would this be in the viewModel?
}