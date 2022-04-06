package com.mafqud.android.base.activity

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.util.other.ContextUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    lateinit var dataStoreManager: DataStoreManager

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

}