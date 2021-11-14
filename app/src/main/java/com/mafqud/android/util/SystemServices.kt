package com.mafqud.android.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager


@SuppressLint("ServiceCast")
fun Context.vibrateMobile(timeInMill: Long = 50) {
    val vibratorManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as Vibrator
    } else {
        this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }
    // Vibrate for 500 milliseconds
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibratorManager!!.vibrate(
            VibrationEffect.createOneShot(
                timeInMill,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        //deprecated in API 26
        vibratorManager!!.vibrate(timeInMill)
    }
}

fun Context.hideKeypad(view: View) {
    // Check if no view has focus:
    val imm: InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)

}

fun Context.showKeypad(view: View) {
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager!!.toggleSoftInputFromWindow(
        view.applicationWindowToken,
        InputMethodManager.SHOW_FORCED, 0
    )

}

fun Activity.isKeyPadIsOpened(): Boolean {
    val imm = this
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    return imm.isActive
}