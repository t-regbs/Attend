package com.example.attend.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.attend.LoginActivity
import com.example.attend.R
import com.example.attend.app.AuthenticationManager
import org.koin.android.ext.android.inject

class LogoutFragment: Fragment() {

    private val handler = Handler()
    private val authenticationManager by inject<AuthenticationManager>()

    private val finishSplash: Runnable = Runnable {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        authenticationManager.clearRegistration()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onStart() {
        super.onStart()
        handler.postDelayed(finishSplash, 1L)
    }

    override fun onStop() {
        handler.removeCallbacks(finishSplash)
        super.onStop()
    }
}