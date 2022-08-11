package com.mafqud.android.util.validation

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.MutableState
import com.mafqud.android.util.other.LogMe
import java.util.regex.Pattern

const val PHONE_MAX_LENGTH = 11
const val NATIONAL_ID_MAX_LENGTH = 14

data class PasswordError(
    var isError: Boolean = false,
    var type: PassErrorType = PassErrorType.NONE
)

enum class PassErrorType {
    INCOMPATIBLE,
    LESS,
    NONE
}

fun isValidEmail(email: String,/* pattern: Pattern = Patterns.EMAIL_ADDRESS*/): Boolean {
    return if (email.isEmpty()) {
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

fun isValidNationalID(id: String): Boolean {
    val isNumeric = id.toBigDecimalOrNull() != null
    return id.length == 14 && id.isNotEmpty() && isNumeric
}

fun isValidPassword(password: String): Boolean {
    val isNumeric = password.toIntOrNull() != null
    return password.length > 8 && password.isNotEmpty() && !isNumeric
}

fun isValidPasswordConfirm(password1: String, password2: String): Boolean {
    return password1 == password2
}

fun isValidName(name: String): Boolean {
    return name.isNotEmpty()
}

/**
 *  yyyy-MM-dd
 */
fun isValidDate(date: String): Boolean {
    return date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
}

fun validatePhoneForm(
    phone: String,
    isPhoneError: MutableState<Boolean>,
    onSuccessValidation: (String) -> Unit
) {

    // check phone validation or any
    if (!isValidPhone(phone)) {
        isPhoneError.value = true
        return
    } else {
        isPhoneError.value = false
    }
    onSuccessValidation(phone)

}

fun validateLoginForm(
    phone: String,
    password: String,
    isPhoneError: MutableState<Boolean>,
    isPasswordError: MutableState<PasswordError>,
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
        LogMe.i("pass", "true")
        isPasswordError.value = PasswordError(isError = true, type = PassErrorType.LESS)
        return
    } else {
        LogMe.i("pass", "false")
        isPasswordError.value = PasswordError(isError = false)
    }
    onSuccessValidation(phone, password)

}

fun validateNationalIDForm(
    id: String,
    isIdError: MutableState<Boolean>,
    onSuccessValidation: (String) -> Unit
) {

    if (!isValidNationalID(id)) {
        isIdError.value = true
        return
    } else {
        isIdError.value = false
    }

    onSuccessValidation(id)

}

fun validatePassAndConfirm(
    password1: String,
    password2: String,
    isPasswordError: MutableState<PasswordError>,
    onValidationSuccess: (String) -> Unit
) {

    // check email validation or any
    if (!isValidPasswordConfirm(password1, password2)) {
        isPasswordError.value = PasswordError(isError = true, type = PassErrorType.INCOMPATIBLE)
        return
    } else {
        isPasswordError.value = PasswordError(isError = false, type = PassErrorType.NONE)
    }
    if (!isValidPassword(password1)) {
        isPasswordError.value = PasswordError(isError = true, type = PassErrorType.LESS)
        return
    } else {
        isPasswordError.value = PasswordError(isError = false, type = PassErrorType.NONE)
    }
    onValidationSuccess(password1)

}

fun validateNameForm(
    name: String,
    isNameError: MutableState<Boolean>,
    onSuccessValidation: (String) -> Unit
) {
    val mName = name.trim()

    // check phone validation or any
    if (!isValidName(mName)) {
        isNameError.value = true
        return
    } else {
        isNameError.value = false
    }

    onSuccessValidation(mName)

}

fun validateNAmeAndEmailForm(
    name: String,
    email: String,
    isNameError: MutableState<Boolean>,
    isEmailError: MutableState<Boolean>,
    onSuccessValidation: (String, String) -> Unit
) {
    val mName = name.trim()
    val mEmail = email.trim()

    // check phone validation or any
    if (!isValidName(mName)) {
        isNameError.value = true
        return
    } else {
        isNameError.value = false
    }

    if (!isValidEmail(mEmail)) {
        isEmailError.value = true
        return
    } else {
        isEmailError.value = false
    }
    onSuccessValidation(mName, mEmail)

}