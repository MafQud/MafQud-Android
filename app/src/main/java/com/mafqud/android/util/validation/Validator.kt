package com.mafqud.android.util.validation

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.MutableState

const val PHONE_MAX_LENGTH = 10

fun isValidEmail(email: String): Boolean {
    return if (TextUtils.isEmpty(email)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

fun isValidPhone(phone: String): Boolean {
    return if (TextUtils.isEmpty(phone) || phone.length < PHONE_MAX_LENGTH) {
        false
    } else {
        Patterns.PHONE.matcher(phone).matches()
    }
}

//TODO pass regex
fun isValidPassword(password: String): Boolean {
    return password.length >= 8 && password.isNotEmpty()
}

fun isValidPasswordConfirm(password1: String, password2: String): Boolean {
    return password1 == password2
}

fun isValidName(name: String): Boolean {
    return name.isNotEmpty()
}


fun validateLoginForm(
    phone: String,
    password: String,
    isPhoneError: MutableState<Boolean>,
    isPasswordError: MutableState<Boolean>,
    onSuccessValidation: (String, String) -> Unit
) {

    // check phone validation or any
    if (!isValidPhone(phone)) {
        isPhoneError.value = true
        return
    } else {
        isPhoneError.value = false
    }

    if (!isValidPassword(password)) {
        isPasswordError.value = true
        return
    } else {
        isPasswordError.value = false
    }
    onSuccessValidation(phone, password)

}