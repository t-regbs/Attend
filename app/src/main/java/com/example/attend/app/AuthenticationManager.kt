package com.example.attend.app

import android.content.SharedPreferences

class AuthenticationManager(val sharedPref: SharedPreferences) {

    fun isAuthenticated(): Boolean =
        sharedPref.getString("username", "")!!.isNotEmpty()

    fun saveRegistration(username: String) {
        sharedPref.edit().putString("username", username).apply()
    }

    fun saveUserType(userType: String) {
        sharedPref.edit().putString("userType", userType).apply()
    }

    fun saveUserId(userId: String) {
        sharedPref.edit().putString("userId", userId).apply()
    }

    fun clearRegistration() {
        sharedPref.edit().remove("username").apply()
    }

    fun getAuthenticatedUser(): String {
        return checkNotNull(sharedPref.getString("username", "").takeIf { it!!.isNotEmpty() })
    }

    fun getUserType(): String? =
        checkNotNull(sharedPref.getString("userType", "").takeIf { it!!.isNotEmpty() })

    fun getUserId(): String = checkNotNull(sharedPref.getString("userId", "").takeIf { it!!.isNotEmpty() })

    var authToken: String = "" // why would this be in the viewModel?
}