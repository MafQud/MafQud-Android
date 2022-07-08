package com.mafqud.android.results.contactSuccess.models
import androidx.annotation.Keep

import com.squareup.moshi.Json


@Keep
data class ContactResponse(
    @field:Json(name = "id")
    val id: Int? = 0 // 4
)