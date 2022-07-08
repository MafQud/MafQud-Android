package com.mafqud.android.results.contact

import com.mafqud.android.util.network.Result


data class ContactViewState(
    val isContactDoneSuccess: Boolean? = null,
    val isContactFailedSuccess: Boolean? = null,
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val networkError: Result.NetworkError? = null,
    val contactID: Int? = null
)