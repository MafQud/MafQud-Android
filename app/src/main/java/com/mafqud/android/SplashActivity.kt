package com.mafqud.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // to be as indicator for the developer to know, whose version he is working on.
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, BuildConfig.VERSION_NAME, Toast.LENGTH_LONG).show()
        }
    }
}