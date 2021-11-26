package com.mafqud.android.util.validation

import android.text.TextUtils
import android.util.Patterns


fun isValidEmail(email: String): Boolean {
    return if (TextUtils.isEmpty(email)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

fun isValidPassword(password: String): Boolean {
    return password.length >=8 && password.isNotEmpty()
}

fun isValidPasswordConfirm(password1: String, password2: String): Boolean {
    return password1 == password2
}

fun isValidName(name: String): Boolean {
    return name.isNotEmpty()
}