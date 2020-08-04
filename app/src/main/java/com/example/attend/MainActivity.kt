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

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val authenticationManager by inject<AuthenticationManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            //Set the toolbar text (I've placed a TextView within the AppBar, which is being referenced here)
            toolbar_text.text = destination.label

            //Set home icon
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled((destination.id in NO_HOME_ICON_FRAGMENTS).not())
                setHomeAsUpIndicator(if (destination.id in TOP_LEVEL_FRAGMENTS)
                    R.drawable.ic_baseline_horizontal_split_24 else R.drawable.ic_baseline_arrow_back_24
                )
            }
        }

//        setupActionBarWithNavController(navController, drawerLayout)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                return if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
                this.findNavController(R.id.nav_host_fragment), drawerLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_student -> {
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.logged_in_graph, true).build()
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_student, null, navOptions)
            }

            R.id.nav_course -> {
                if(isValidDestination(R.id.nav_course)){
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_course)
                }
            }

            R.id.nav_lecturer -> {
                if(isValidDestination(R.id.nav_lecturer)){
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_lecturer)
                }
            }

            R.id.nav_attendance -> {
                if(isValidDestination(R.id.nav_attendance)){
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_attendance)
                }
            }

            R.id.nav_logout -> {
                authenticationManager.clearRegistration()
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.logged_out_graph, true).build()
                findNavController(R.id.nav_host_fragment).navigate(R.id.logged_out_graph, null, navOptions)
            }
        }
        item.isChecked = true
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun isValidDestination(destination : Int):Boolean{
        return destination != findNavController(R.id.nav_host_fragment).currentDestination?.id
    }
}