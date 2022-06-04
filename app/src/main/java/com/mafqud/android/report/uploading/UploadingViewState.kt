package com.mafqud.android.report.uploading

import android.net.Uri
import com.mafqud.android.report.uploading.models.CreateCaseBody

/**
 * 1- set -> isUploadingImages -> true
 * if (success) than imagesUrlsUploaded -> data , isUploadingImages -> false
 * if (failed) than imagesUrlsUploaded -> null , isUploadingImages -> false
 * 2- isUploadingCase -> true
 * isSuccess -> true  , isUploadingCase -> false
 */
data class UploadingViewState(
    val isUploadingImages: Boolean = false,
    val isUploadingCase: Boolean = false,
    val isFailedUploadingImages: Boolean = false,
    val isSuccessUploadingImages: Boolean = false,

    val isFailedUploadingCase: Boolean = false,
    val isSuccessUploadingCase: Boolean = false,

    val imagesUrisPicked: List<Uri> = emptyList(),
    val imagesUrlsUploaded: UploadingRepository.UploadImagesIDs? = null,
    val createCaseBody: CreateCaseBody? = null,
    val isSuccess: Boolean? = null,

    )

data class CaseItem(
    val name: String = "",
    val date: String = "",
    val time: String = "",
    val state: String = "",
)