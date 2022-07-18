package com.mafqud.android.util.dateFormat

import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.text.SimpleDateFormat
import java.util.*

/**
 * from: 2022-05-23T10:58:08.452511Z
 * to : March,13, 2022 at 3:15 PM
 */

fun fromFullDateToAnother(fromFormat: String?): String {
    fromFormat?.let {
       try {
           val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
           val date: Date? = formatter.parse(it)

           val fmtOut = SimpleDateFormat("dd, MMM, yyyy  h:mm a",  Locale.getDefault())
           return fmtOut.format(date!!)

       } catch (e: Exception) {
           FirebaseCrashlytics.getInstance().log("Can't parse date: "+ e.localizedMessage)
           return "Error parse"
       }
    }
    return ""
}

/**
 * from: 2022-05-23T10:58:08.452511Z
 * to : March,13, 2022 at 3:15 PM
 */

fun fromNormalDateToFull(fromFormat: String?): String {
    fromFormat?.let {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date? = formatter.parse(it)

            val fmtOut = SimpleDateFormat("dd, MMM, yyyy",  Locale.getDefault())
            return fmtOut.format(date!!)

        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("Can't parse date: "+ e.localizedMessage)
            return "Error parse"
        }
    }
    return ""
}

/**
 * from: 2021-04-29
 * to : Mon, 1999/07/12
 */
fun fromGlobalToDisplay(globalDate: String?): String {
    globalDate?.let {
        if (globalDate.isEmpty()) return ""
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date = formatter.parse(it)

            val fmtOut = SimpleDateFormat("EEE, yyyy/MM/dd", Locale.getDefault())
            return fmtOut.format(date)

        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("Can't parse date: "+ e.localizedMessage)
            return "Error parse"        }
    }
    return ""
}