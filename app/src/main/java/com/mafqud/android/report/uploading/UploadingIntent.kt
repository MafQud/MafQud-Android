package com.mafqud.android.report.uploading

import android.net.Uri

sealed class UploadingIntent {

    data class UploadCaseWithImages(
        val caseItem: CaseItem,
        val imagesUrisPicked: List<Uri> = emptyList()
    ) : UploadingIntent()

    object UploadCaseOnly : UploadingIntent()
    object UploadImagesOnly : UploadingIntent()
}
