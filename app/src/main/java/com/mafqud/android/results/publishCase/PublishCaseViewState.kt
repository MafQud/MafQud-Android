package com.mafqud.android.results.publishCase

import com.mafqud.android.util.network.Result


data class PublishCaseViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val networkError: Result.NetworkError? = null,
)