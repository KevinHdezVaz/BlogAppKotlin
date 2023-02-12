package com.kevin.courseApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kevin.courseApp.core.hide
import com.kevin.courseApp.core.show
import com.kevin.courseApp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
     private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        observeDestinationChange()



    }

    private fun observeDestinationChange() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigation.hide()
                }

                R.id.registerFragment -> {
                    binding.bottomNavigation.hide()
                }

                R.id.profileFragment -> {
                    binding.bottomNavigation.show()
                }


                else -> {
                    binding.bottomNavigation.show()
                }
            }
        }
    }


}
