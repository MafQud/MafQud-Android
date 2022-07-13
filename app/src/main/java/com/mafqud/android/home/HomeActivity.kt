package com.mafqud.android.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.compose.runtime.MutableState
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mafqud.android.R
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.util.other.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {


    private lateinit var isHomeBarVisible: MutableState<Boolean>
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarColor(resources.getColor(R.color.gray_status))
        setContentView(R.layout.activity_home)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_nav)

        //setup
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.reportFragment,
                R.id.mapFragment,
                R.id.moreFragment ->  {
                    bottomNavigationView.isVisible = true
                }
                else -> {
                    bottomNavigationView.isVisible = false
                }
            }
        }
        // listen on (bottomNavigationView) item clicked
        //onNavigationBottomItemClicked(bottomNavigationView, isHomeBarVisible)
    }

    private fun onNavigationBottomItemClicked(
        bottomNavigationView: BottomNavigationView,
        isHomeBarVisible: MutableState<Boolean>
    ) {
        bottomNavigationView.setOnItemSelectedListener { menu ->

            when (menu.itemId) {
                R.id.homeFragment,
                R.id.reportFragment,
                R.id.mapFragment -> {
                    isHomeBarVisible.value = true
                    navigateToDestination(menu)
                    true
                }
                R.id.moreFragment,
                R.id.notificationFragment -> {
                    isHomeBarVisible.value = false
                    navigateToDestination(menu)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun navigateToDestination(menu: MenuItem) {
        try {
            NavigationUI.onNavDestinationSelected(menu, navHostFragment.navController)
        } catch (e: Exception) {

        }
    }

    fun homeBarVisibility(isVisible: Boolean) {
        //isHomeBarVisible.value = isVisible
    }

    fun bottomBarNavigationVisibility(isVisible: Boolean) {
        bottomNavigationView.isVisible = isVisible
    }

    fun bottomNavigationBarVisibility(isVisible: Boolean = true) {
        if (isVisible) {
            bottomNavigationView.visibility = View.VISIBLE
        } else
            bottomNavigationView.visibility = View.GONE

    }
}