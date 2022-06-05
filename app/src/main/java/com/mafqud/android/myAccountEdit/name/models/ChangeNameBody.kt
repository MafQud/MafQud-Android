package com.mafqud.android.myAccountEdit.name.models
import androidx.annotation.Keep

import com.squareup.moshi.Json


@Keep
data class ChangeNameBody(
    @field:Json(name = "name")
    val name: String? = "" // Osama Yasser
)