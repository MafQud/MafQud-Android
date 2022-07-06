package com.mafqud.android

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapp.MyEventBusIndex
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class MyApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // TODO enable dark theme in next release
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        EventBus.builder()
            // have a look at the index class to see which methods are picked up
            // if not in the index @Subscribe methods will be looked up at runtime (expensive)
            .addIndex(MyEventBusIndex())
            .installDefaultEventBus()
    }

    fun context(): Context = applicationContext

}