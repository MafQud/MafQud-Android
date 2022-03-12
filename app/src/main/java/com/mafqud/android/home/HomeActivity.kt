package com.mafqud.android.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mafqud.android.R
import com.mafqud.android.base.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {


    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_nav)

        //setup
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        // listen on (bottomNavigationView) item clicked
        // onNavigationBottomItemClicked(bottomNavigationView)
    }


  /*  private fun onNavigationBottomItemClicked(
        bottomNavigationView: BottomNavigationView
    ) {
        bottomNavigationView.setOnItemSelectedListener { menu ->

            when (menu.itemId) {
                R.id.homeFragment -> {
                    navigateToDestination(menu)
                    true
                }
                R.id.exploreFragment,
                R.id.offersFragment,
                R.id.mapFragment,
                R.id.profileFragment ->{
                }
                else -> {
                    false
                }
            }


        }
    }*/

    private fun navigateToDestination(menu: MenuItem) {
        try {
            NavigationUI.onNavDestinationSelected(menu, navHostFragment.navController)
        } catch (e: Exception) {

        }
    }

    fun bottomNavigationBarVisibility(isVisible: Boolean = true) {
        if (isVisible) {
            bottomNavigationView.visibility = View.VISIBLE
        } else
            bottomNavigationView.visibility = View.GONE

    }
}