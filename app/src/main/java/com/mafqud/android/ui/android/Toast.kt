package com.mafqud.android.ui.android

import android.content.Context
import android.widget.Toast


fun Context.showToast(message: String = "", period: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, period).show()
}


