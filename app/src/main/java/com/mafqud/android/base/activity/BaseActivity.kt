package com.mafqud.android.base.activity

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.mafqud.android.R
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.LogoutHelper
import com.mafqud.android.ui.compose.ShouldLogoutDialog
import com.mafqud.android.util.other.ContextUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var logoutHelper: LogoutHelper

    enum class LangType {
        AR, EN
    }


    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        // To change app lang
        if (!this::dataStoreManager.isInitialized) {
            dataStoreManager = DataStoreManager(newBase)
        }
        val lang = runBlocking {
            return@runBlocking dataStoreManager.read(
                DataStoreManager.LANGUAGE,
                LangType.AR.toString()
            )
        }
        val myLocale = Locale(lang)
        val localeUpdatedContext: ContextWrapper = ContextUtil.updateLocale(newBase, myLocale)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onUnauthorizedUser(message: String) {
        // logout current user
        logOut()
    }

    private fun logOut() {
        lifecycleScope.launch {
            logoutHelper.logOutUser(this@BaseActivity)
        }
    }

}