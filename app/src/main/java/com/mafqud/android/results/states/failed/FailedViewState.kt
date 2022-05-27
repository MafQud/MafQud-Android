package com.mafqud.android.results.states.failed

import com.mafqud.android.util.network.Result


data class FailedViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val networkError: Result.NetworkError? = null,
)