package com.mafqud.android.myAccountEdit.name

import com.mafqud.android.util.network.Result

data class AccountViewState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val errorFieldMessage: String? = null,
    val userName: String? = null,
    val networkError: Result.NetworkError? = null,
)