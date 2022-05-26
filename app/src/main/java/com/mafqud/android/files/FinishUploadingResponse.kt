package com.mafqud.android.files
import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class FinishUploadingResponse(
    @field:Json(name = "id")
    val id: Int = 0, // 10
)