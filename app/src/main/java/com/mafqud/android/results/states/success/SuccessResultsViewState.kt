package com.mafqud.android.results.states.success

import com.mafqud.android.util.network.Result


data class SuccessResultsViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val networkError: Result.NetworkError? = null,
    val nationalID: String = ""
)