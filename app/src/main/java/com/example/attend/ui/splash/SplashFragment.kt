package com.example.attend.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.attend.R
import com.example.attend.SplashGraphDirections
import com.example.attend.app.AuthenticationManager
import org.koin.android.ext.android.inject

class SplashFragment: Fragment() {

    private val handler = Handler()
    private val authenticationManager by inject<AuthenticationManager>()

    private val finishSplash: Runnable = Runnable {
        if (authenticationManager.isAuthenticated()) {
            Navigation.findNavController(requireView()).navigate(
                    SplashGraphDirections.splashToLoggedIn(
                        authenticationManager.getAuthenticatedUser()
                    )
                )
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.splash_to_logged_out)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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