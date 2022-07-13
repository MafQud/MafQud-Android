package com.mafqud.android.util

import java.text.NumberFormat
import java.util.*

fun Int?.toLocalizedNumber(): String {
    val formatting =
        NumberFormat.getInstance(Locale.getDefault())

    val formattedAge = if (this != null) {
        formatting.format(this)
    } else ""

    return formattedAge
}