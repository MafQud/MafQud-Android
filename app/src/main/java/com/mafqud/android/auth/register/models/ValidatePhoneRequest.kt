package com.mafqud.android.auth.register.models
import androidx.annotation.Keep

import com.squareup.moshi.Json


@Keep
data class ValidatePhoneRequest(
    @field:Json(name = "phone")
    val phone: String? = "" // 1234567899
)