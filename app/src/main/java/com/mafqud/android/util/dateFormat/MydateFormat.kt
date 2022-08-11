package com.mafqud.android.util.dateFormat

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mafqud.android.util.validation.isValidDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * from: 2022-05-23T10:58:08.452511Z
 * to : March,13, 2022 at 3:15 PM
 */

fun fromFullDateToAnother(
    fromFormat: String?, locale: Locale = Locale.getDefault(),
    reportToFirebase: Boolean = true
): String {
    fromFormat?.let {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale)
            val date: Date? = formatter.parse(it)

            val fmtOut = SimpleDateFormat("dd, MMM, yyyy  h:mm a", locale)
            return fmtOut.format(date!!)

        } catch (e: Exception) {
            if (reportToFirebase)
                FirebaseCrashlytics.getInstance().log("Can't parse date: " + e.localizedMessage)
            return "Error parse"
        }
    }
    return ""
}

/**
 * from: yyyy-MM-dd
 * to : dd, MMM, yyyy
 */

fun fromNormalDateToFull(
    fromFormat: String?, locale: Locale = Locale.getDefault(),
): String {
    fromFormat?.let {
        if (!isValidDate(it)) return "Error parse"
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", locale)
            val date: Date? = formatter.parse(it)

            val fmtOut = SimpleDateFormat("dd, MMM, yyyy", locale)
            return fmtOut.format(date!!)

        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("Can't parse date: " + e.localizedMessage)
            return "Error parse"
        }
    }
    return ""
}

/**
 * from: 2021-04-29
 * to : Mon, 1999/07/12
 */
fun fromGlobalToDisplay(
    globalDate: String?, locale: Locale = Locale.getDefault(),
): String {
    globalDate?.let {
        if (!isValidDate(it)) return "Error parse"
        if (globalDate.isEmpty()) return ""
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", locale)
            val date: Date = formatter.parse(it)

            val fmtOut = SimpleDateFormat("EEE, yyyy/MM/dd", locale)
            return fmtOut.format(date)

        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("Can't parse date: " + e.localizedMessage)
            return "Error parse"
        }
    }
    return ""
}