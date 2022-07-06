package com.mafqud.android.data

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.mafqud.android.auth.AuthActivity
import com.mafqud.android.di.MyServiceInterceptor
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class LogoutHelper @Inject constructor() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var myServiceInterceptor: MyServiceInterceptor


    suspend fun logOutUser(currentActivity: Activity) {
        // clear the current user token from interceptor
        myServiceInterceptor.setSessionToken("")
        // clear user data
        dataStoreManager.clearDataStore()
        // restart application
        currentActivity.startActivity(Intent(currentActivity, AuthActivity::class.java))
        currentActivity.finishAffinity()
    }
}
