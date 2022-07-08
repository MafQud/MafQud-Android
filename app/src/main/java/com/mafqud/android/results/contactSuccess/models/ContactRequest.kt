package com.mafqud.android.results.contactSuccess.models
import androidx.annotation.Keep

import com.squareup.moshi.Json


@Keep
data class ContactRequest(
    @field:Json(name = "case_id")
    val caseId: Int? = 0 // 4
)