package com.example.attend

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.attend.app.AuthenticationManager
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.android.inject

class LecturerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val authenticationManager by inject<AuthenticationManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupNav()
    }

    private fun setupNav() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.lecturer_nav_host_fragment)
        val topDestinations = setOf(
            R.id.nav_home,
            R.id.nav_view_attendance,
            R.id.nav_lecturer_students,
            R.id.nav_take_attendance)
        appBarConfiguration =
            AppBarConfiguration(topLevelDestinationIds = topDestinations, drawerLayout = drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            android.R.id.home -> {
//                return if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    true
//                } else {
//                    drawerLayout.openDrawer(GravityCompat.START)
//                    true
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.lecturer_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}