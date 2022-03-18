package com.mafqud.android.auth.register

import com.mafqud.android.util.network.Result


data class RegisterViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isValidPhone: Boolean = false,
    val isValidEmail: Boolean = false,
    val phone: String? = null,
    val email: String? = null,

    val errorFieldMessage: String? = null,
    val data: String? = null,
    val networkError: Result.NetworkError? = null,
    val enteredPhone: String = ""
)