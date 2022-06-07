package com.mafqud.android.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mafqud.android.R
import com.mafqud.android.auth.AuthActivity
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.data.DataStoreManager.Companion.IS_PASSED_INTRO
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.other.inAppUpdate
import com.mafqud.android.util.other.statusBarColor

const val SPLASH_DELAY = 3500L

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private var isPassed: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        statusBarColor(resources.getColor(R.color.white))
        initialSetup()
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            LogMe.i("firebaseToken", token)
        })*/
    }

    override fun onResume() {
        super.onResume()
        // We display our landing activity edge to edge just like the splash screen
        // to have a seamless transition from the system splash screen.
        // This is done in onResume() so we are sure that our Activity is attached
        // to its window.
        //WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun initialSetup() {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        checkIfUserPassedIntro()
        //getHashKey()
        setContent {
            /*// Update the system bars to be translucent
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
            }*/
            MafQudTheme {
                SplashScreen {}
                Handler(Looper.getMainLooper()).postDelayed({
                    startApplication()
                }, SPLASH_DELAY)
            }
        }
        // check google play for our app update
        inAppUpdate()
    }

    private fun checkIfUserPassedIntro() {
        lifecycleScope.launchWhenCreated {
            //read data from data store
            isPassed = dataStoreManager.isLoggedIn()
        }
    }

    private fun startApplication() {
        if (isPassed) {
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        } else
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
        finish()

    }
}