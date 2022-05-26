package com.mafqud.android.report.lost

import android.net.Uri
import com.mafqud.android.locations.CitiesResponse
import com.mafqud.android.locations.GovResponse
import com.mafqud.android.locations.MyCity
import com.mafqud.android.locations.MyGov
import com.mafqud.android.util.network.Result


data class ReportViewState(
    val isLoading: Boolean = false,
    val errorFieldMessage: String? = null,
    val imagesUri: List<Uri> = emptyList(),
    val networkError: Result.NetworkError? = null,
    /**
     * ui data
     */
    val govs: List<MyGov>? = null,
    val cities: List<MyCity>? = null,
    val cityId: Int? = null,
    val govId: Int? = null,
)