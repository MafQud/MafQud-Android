package com.mafqud.android.base


sealed class GeneralUiState<out T> {
    data class Loading(var isLoading: Boolean) : GeneralUiState<Nothing>()
    data class Refresh(var isRefresh: Boolean) : GeneralUiState<Nothing>()
    data class Error(var responseError: ResponseError) : GeneralUiState<Nothing>()
    data class Data<T>(var data: T) : GeneralUiState<T>()
}

sealed class ResponseError {
    object Network : ResponseError()
    data class Generic(var message: String) : ResponseError()
}