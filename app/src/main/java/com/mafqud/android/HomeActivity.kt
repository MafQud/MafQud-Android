package com.mafqud.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mafqud.android.ui.material.BoxUi
import com.mafqud.android.ui.material.ColumnUi
import com.mafqud.android.ui.material.TextUi

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        setContent {
            // to be as indicator for the users to know, whose version he is working on.
            BoxUi(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                ColumnUi(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextUi(text = BuildConfig.VERSION_NAME, fontSize = 18.sp)
                    TextUi(text = stringResource(id = R.string.note_release), fontSize = 14.sp)

                }
            }
        }


    }
}