package com.mafqud.android.auth.login


sealed class LoginIntent {
    data class PhoneLogin(val phone: String, val password: String) : LoginIntent()
}