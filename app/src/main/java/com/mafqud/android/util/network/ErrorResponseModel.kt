package com.mafqud.android.util.network

import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class ErrorResponseModel(
    @field:Json(name = "data")
    val `data`: Any? = null, // null
    @field:Json(name = "message")
    val message: String? = null, // The email must be a valid email address.
    @field:Json(name = "status")
    val status: Int? = null // 422
)