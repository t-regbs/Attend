package com.example.attend.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.attend.LecturerActivity
import com.example.attend.R
import com.example.attend.SplashGraphDirections
import com.example.attend.app.AuthenticationManager
import org.koin.android.ext.android.inject

class RouteFragment: Fragment() {

    private val handler = Handler()
    private lateinit var userType: String

    private val finishSplash: Runnable = Runnable {
        if (userType == "Lecturer") {
            activity?.startActivity(Intent(activity,LecturerActivity::class.java))
        } else {
            Navigation.findNavController(requireView()).navigate(
                    RouteFragmentDirections.actionNavRoutingToNavStudent()
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: RouteFragmentArgs by navArgs()
        userType = safeArgs.userType

        return inflater.inflate(R.layout.fragment_route, container, false)
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