package com.mafqud.android.home

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mafqud.android.R
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.ui.compose.HomeAppBar
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.CreateAndroidViewForXMLLayout
import com.mafqud.android.ui.theme.MafQudTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {


    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MafQudTheme {
                ColumnUi {
                    HomeAppBar(onNotificationClicked = {
                        // open notification fragment
                        if (::navHostFragment.isInitialized) {
                            navHostFragment.navController.navigate(R.id.action_notification)
                        }
                    })
                    NavigationHostFragment()
                }
            }
        }
    }

    @Composable
    private fun NavigationHostFragment() {
        val layout = CreateAndroidViewForXMLLayout(
            R.layout.activity_home,
            Modifier.fillMaxSize()
        )
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = layout.findViewById(R.id.bottom_nav)
        //setup
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

    fun bottomNavigationBarVisibility(isVisible: Boolean = true) {
        if (isVisible) {
            bottomNavigationView.visibility = View.VISIBLE
        } else
            bottomNavigationView.visibility = View.GONE

    }
}