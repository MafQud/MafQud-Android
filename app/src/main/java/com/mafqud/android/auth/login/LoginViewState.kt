package com.mafqud.android.auth.login

import com.mafqud.android.util.network.Result


data class LoginViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val data: String? = null,
    val networkError: Result.NetworkError? = null,
    val enteredPhone: String = ""
)