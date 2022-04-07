package com.mafqud.android.report.lost

import android.net.Uri


sealed class LostIntent {
    data class SaveImagesUri(val imagesUri: List<Uri> = emptyList()) : LostIntent()
    data class RemoveImageUri(val imagesUri: Uri? = null) : LostIntent()
}