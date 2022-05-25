package com.mafqud.android.util.dateFormat

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
           val date: Date = formatter.parse(it)

           val fmtOut = SimpleDateFormat("dd, MMM, yyyy  h:mm a")
           return fmtOut.format(date)

       } catch (e: Exception) {
           return "Can't parse date"
       }
    }
    return ""
}