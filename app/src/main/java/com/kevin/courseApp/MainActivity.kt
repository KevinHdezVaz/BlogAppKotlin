package com.kevin.courseApp

import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kevin.courseApp.core.hide
import com.kevin.courseApp.core.show
import com.kevin.courseApp.databinding.ActivityMainBinding
import com.onesignal.OneSignal


class MainActivity : AppCompatActivity() {
    private val ONESIGNAL_APP_ID = "cc90886d-84ad-4966-ac52-e12cc0208c22"

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

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);


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
                R.id.masEstudianteFragment -> {
                    binding.bottomBar.hide()
                }

                R.id.allCoursesFragment -> {
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