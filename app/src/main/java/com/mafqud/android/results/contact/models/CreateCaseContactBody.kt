package com.mafqud.android.results.contact.models
import androidx.annotation.Keep

import com.squareup.moshi.Json


@Keep
data class CreateCaseContactBody(
    @field:Json(name = "case_id")
    val caseId: Int? = 0 // 1
)