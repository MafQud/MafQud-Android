package com.mafqud.android.auth.register


sealed class RegisterIntent {
    data class ValidatePhone(val phone: String) : RegisterIntent()
    data class ValidateEmail(val email: String) : RegisterIntent()
    data class Signup(
        val phone: String = "",
        val email: String = "",
        val fullName: String = "",
        val govId: Int = -1,
        val cityId: Int = -1,
        val password: String = "",
    ) : RegisterIntent()

    object DisplayGovAndCity : RegisterIntent()
}