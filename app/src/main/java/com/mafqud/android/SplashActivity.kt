package com.mafqud.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.mafqud.android.ui.material.BoxUi
import com.mafqud.android.ui.material.TextUi

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        // to be as indicator for the developer to know, whose version he is working on.
        setContent {
            BoxUi(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                TextUi(text = BuildConfig.VERSION_NAME, fontSize = 20.sp)
            }
        }
    }
}