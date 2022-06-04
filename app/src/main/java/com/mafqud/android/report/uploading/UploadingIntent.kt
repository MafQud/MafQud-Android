package com.mafqud.android.report.uploading

import android.net.Uri
import com.mafqud.android.report.uploading.models.CreateCaseBody

sealed class UploadingIntent {

    data class UploadCaseWithImages(
        val createCaseBody: CreateCaseBody,
        val imagesUrisPicked: List<Uri> = emptyList()
    ) : UploadingIntent()

    object UploadCaseOnly : UploadingIntent()
    object UploadImagesOnly : UploadingIntent()
}
