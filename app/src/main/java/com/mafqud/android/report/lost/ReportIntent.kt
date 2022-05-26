package com.mafqud.android.report.lost

import android.net.Uri


sealed class ReportIntent {
    object GetGovs : ReportIntent()
    data class GetCities(val cityId: Int) : ReportIntent()
    data class SaveImagesUri(val imagesUri: List<Uri> = emptyList()) : ReportIntent()
    data class RemoveImageUri(val imagesUri: Uri? = null) : ReportIntent()
}