package com.mafqud.android.auth.register

import com.mafqud.android.report.lost.ReportIntent


sealed class RegisterIntent {
    data class ValidatePhone(val phone: String) : RegisterIntent()
    data class ValidateEmail(val email: String) : RegisterIntent()
    /*object Signup*//*(
        val phone: String = "",
        val email: String = "",
        val fullName: String = "",
        val govId: Int = -1,
        val cityId: Int = -1,
        val password: String = "",
    )*//* : RegisterIntent()*/
    data class GetCities(val cityId: Int) : RegisterIntent()
    data class SaveName(val name: String) : RegisterIntent()
    data class SavePassword(val password: String) : RegisterIntent()
    data class NextStep(val stepCount: StepCount) : RegisterIntent()
    object Clear : RegisterIntent()
    data class VerifyOTP(val loading: Boolean = false) : RegisterIntent()
    data class SaveLocation(val govId: Int,val cityId: Int) : RegisterIntent()
}