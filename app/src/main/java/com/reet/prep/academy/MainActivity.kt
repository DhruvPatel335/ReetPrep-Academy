package com.reet.prep.academy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.reet.prep.academy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.reet.prep.academy.fragments.PurchasedCourse
import com.reet.prep.academy.fragments.CurrentAffair
import com.reet.prep.academy.fragments.Home

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawer: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_content_main
        ) as NavHostFragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        navController = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemReselectedListener {
            while (!isStartDestination()) {
                navController.popBackStack()
            }
        }
        bottomNavigationView.setOnItemReselectedListener {
            NavigationUI.onNavDestinationSelected(it, navController) }
        initDrawer()
        binding.nvDrawer.setNavigationItemSelectedListener (onNavigationItemSelected)
    }

    private fun isStartDestination(): Boolean {
        return when (navController.currentDestination!!.label) {
            CurrentAffair().javaClass.simpleName -> {
                true
            }

            PurchasedCourse().javaClass.simpleName -> {
                true
            }
            Home().javaClass.simpleName -> {
                true
            }

            else -> {
                false
            }
        }
    }

    private fun initDrawer() {
        drawer = binding.dlDrawer
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close)
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                //Empty overridden method
            }

            override fun onDrawerOpened(drawerView: View) {
//                binding.ivMenu.setImageResource(R.drawable.ic_close_24)
            }

            override fun onDrawerClosed(drawerView: View) {
//                binding.ivMenu.setImageResource(R.drawable.ic_side_menu_24)
            }

            override fun onDrawerStateChanged(newState: Int) {
                //Empty overridden method
            }
        })
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val onNavigationItemSelected =
        NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myProfile -> {
                    navController.navigate(R.id.action_homeNavigation_to_myProfile)
                    drawer.close()
                }
                R.id.logOut -> {

                }
            }
            true
        }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}