package com.mafqud.android.util.other

import android.content.Context
import java.util.*

/**
 * Created by sara.elsegeny on 11/8/2015.
 */
object Localization {
    fun setLanguage(ctx: Context, tag: String) {
        if (tag.equals("en", ignoreCase = true)) {
            setLocale(ctx, "en")
        } else {
            setLocale(ctx, "ar")
        }
    }

    private fun setLocale(ctx: Context, lang: String) {
        val myLocale = Locale(lang)
        val res = ctx.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }
}