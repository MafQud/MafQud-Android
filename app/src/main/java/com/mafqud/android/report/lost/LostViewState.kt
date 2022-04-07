package com.mafqud.android.report.lost

import android.net.Uri
import com.mafqud.android.util.network.Result


data class LostViewState(
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val imagesUri: List<Uri> = emptyList(),
    val networkError: Result.NetworkError? = null,
)