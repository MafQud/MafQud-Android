package com.mafqud.android

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.TextUi
import com.mafqud.android.util.other.inAppUpdate


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialSetup()
        setContent {
            // to be as indicator for the users to know, whose version he is working on.
            BoxUi(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                ColumnUi(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextUi(
                        text = BuildConfig.VERSION_NAME.lowercase(),
                        fontSize = 18.sp
                    )
                    if (!BuildConfig.IS_STABLE) {
                        TextUi(text = stringResource(id = R.string.note_release), fontSize = 14.sp)
                    }
                }
            }

        }

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

    private fun initialSetup() {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        // check google play for our app update
        inAppUpdate()
    }
}