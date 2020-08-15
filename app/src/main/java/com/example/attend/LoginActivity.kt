package com.example.attend

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.ui.*
import com.example.attend.app.AuthenticationManager
import com.example.attend.utils.NO_HOME_ICON_FRAGMENTS
import com.example.attend.utils.TOP_LEVEL_FRAGMENTS
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {


    private val authenticationManager by inject<AuthenticationManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

}