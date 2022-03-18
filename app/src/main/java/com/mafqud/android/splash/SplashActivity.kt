package com.mafqud.android.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mafqud.android.auth.AuthActivity
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.DataStoreManager.Companion.IS_PASSED_INTRO
import com.mafqud.android.data.DataStoreManager.Companion.LANGUAGE
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.theme.MafQudTheme
import com.mafqud.android.util.other.changLanguage
import com.mafqud.android.util.other.inAppUpdate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val SPLASH_DELAY = 4000L

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private var isPassed: Boolean = true

    enum class LangType {
        AR, EN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        initialSetup()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("firebaseToken", token)
        })


    }

    override fun onResume() {
        super.onResume()
        // We display our landing activity edge to edge just like the splash screen
        // to have a seamless transition from the system splash screen.
        // This is done in onResume() so we are sure that our Activity is attached
        // to its window.
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun initialSetup() {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        checkIfUserPassedIntro()
        changeApplicationLanguage()
        //getHashKey()
        setContent {
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

    private fun changeApplicationLanguage() {
        // To change app lang
        lifecycleScope.launchWhenCreated {
            val lang = dataStoreManager.read(LANGUAGE, LangType.AR.toString())
            changLanguage(lang)
        }
    }

    private fun checkIfUserPassedIntro() {
        lifecycleScope.launchWhenCreated {
            //read data from data store
            isPassed = dataStoreManager.read(IS_PASSED_INTRO, false)
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