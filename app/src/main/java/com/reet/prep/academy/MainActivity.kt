package com.reet.prep.academy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.reet.prep.academy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.reet.prep.academy.fragments.PurchasedCourse
import com.reet.prep.academy.fragments.CurrentAffair
import com.reet.prep.academy.fragments.Home
import com.reet.prep.academy.repository.AuthenticationRepository
import com.reet.prep.academy.repository.TestSeriesRepository
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultListener {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawer: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var userId: String
    private lateinit var courseID: String
    private lateinit var phoneNumber: String
    private val authenticationRepository = AuthenticationRepository()
    lateinit var actionBar: ActionBar
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

        binding.ivMenu.setOnClickListener {
            onMenuClicked()
        }
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
    }

    private fun onMenuClicked() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END)
        } else {
            drawer.openDrawer(GravityCompat.END)
        }
    }

    private val onNavigationItemSelected =
        NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myProfile -> {
                    safeNavigate(navController,R.id.action_homeNavigation_to_myProfile)
                    drawer.close()
                }

                R.id.logOut -> {
                    FirebaseAuth.getInstance().signOut()
                    finish()
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
        checkout.setImage(R.mipmap.ic_launcher);
        checkout.open(this, jsonObject)
    }

    fun safeNavigate(navController: NavController, direction: Int, bundle: Bundle? = null) {
        navController.currentDestination?.getAction(direction)?.run {
            navController.navigate(direction, bundle)
        }
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