package com.mafqud.android.util.network

import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class ErrorResponseModel(
    @field:Json(name = "detail")
    val detail: Detail? = Detail(),
    @field:Json(name = "message")
    val message: String? = "", // No active account found with the given credentials
    @field:Json(name = "status_code")
    val statusCode: Int? = 0 // 401
) {
    @Keep
    class Detail
}