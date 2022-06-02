package com.mafqud.android.auth.register

import com.mafqud.android.locations.MyCity
import com.mafqud.android.locations.MyGov
import com.mafqud.android.util.network.Result


data class RegisterViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isValidPhone: Boolean? = null,
    /* val isValidEmail: Boolean? = false,*/
    val phone: String? = null,
    val name: String? = null,
    val password: String? = null,
    val stepCount: StepCount = StepCount.One,
    val govs: List<MyGov>? = null,
    val cities: List<MyCity>? = null,
    val cityId: Int? = null,
    val govId: Int? = null,
    val errorFieldMessage: String? = null,
    val networkError: Result.NetworkError? = null,
    val enteredPhone: String = ""
)