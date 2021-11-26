package com.mafqud.android.ui.other

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.mafqud.android.util.other.wordCount


fun snakeBarMessage(
    view: View,
    message: String,
    period: Int = if (message.wordCount() < 5) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
) {
    Snackbar.make(view, message, period).show()
}


