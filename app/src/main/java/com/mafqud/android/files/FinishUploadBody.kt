package com.mafqud.android.files

import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class FinishUploadBody(
    @field:Json(name = "file_id")
    val fileId: String = "" // 1
)


