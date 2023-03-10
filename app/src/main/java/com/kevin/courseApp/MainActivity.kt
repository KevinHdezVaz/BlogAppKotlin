package com.kevin.courseApp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase
import com.kevin.courseApp.core.hide
import com.kevin.courseApp.core.show
import com.kevin.courseApp.databinding.ActivityMainBinding
import com.kevin.courseApp.utils.NotificacionReceiver


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupSmoothBottomMenu()
        observeDestinationChange()




    }

    private fun setupSmoothBottomMenu() {
        val popupmenu = PopupMenu(this, null)
        popupmenu.inflate(R.menu.menu_botom)
        val menu: Menu = popupmenu.getMenu()
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    private fun observeDestinationChange() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.loginFragment -> {
                    binding.bottomBar.hide()
                }

                R.id.registerFragment -> {
                    binding.bottomBar.hide()
                }
                R.id.categoria_Detalles -> {
                    binding.bottomBar.hide()
                }

                R.id.introFragment2 -> {
                    binding.bottomBar.hide()
                }
                else -> {
                    binding.bottomBar.show()
                }
            }
        }
    }
    fun hideSmoothBottomBar() {
        // oculta el SmoothBottomBar
        binding.bottomBar.hide()

    }

    fun showSmooth() {
        // oculta el SmoothBottomBar
        binding.bottomBar.show()

    }
}