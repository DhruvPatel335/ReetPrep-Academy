package com.reet.prep.academy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.reet.prep.academy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.reet.prep.academy.fragments.PurchasedCourse
import com.reet.prep.academy.fragments.CurrentAffair
import com.reet.prep.academy.fragments.Home
import com.reet.prep.academy.repository.AuthenticationRepository
import com.reet.prep.academy.repository.TestSeriesRepository
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawer: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var userId: String
    private lateinit var courseID: String
    private lateinit var phoneNumber: String
    private val authenticationRepository = AuthenticationRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Checkout.preload(applicationContext);

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
            NavigationUI.onNavDestinationSelected(it, navController)
        }
        initDrawer()
        binding.nvDrawer.setNavigationItemSelectedListener(onNavigationItemSelected)
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

    fun pay(jsonObject: JSONObject, paymentInitiator: JSONObject) {
        courseID = paymentInitiator.getString("courseId")
        userId = paymentInitiator.getString("uid")
        phoneNumber = paymentInitiator.getString("phoneNumber")
        val checkout: Checkout = Checkout()
        checkout.setKeyID("rzp_test_JQHZQjLCC0MGLN")
        checkout.setImage(R.drawable.brand_icon);
        checkout.open(this, jsonObject)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        Log.e("Payment Success", p0.toString())
        Log.e("userId", userId)
        Log.e("courseId", courseID)
        authenticationRepository.addPurchase(courseID, phoneNumber, userId)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        Log.e("Payment Failed", p0.toString())
    }
}