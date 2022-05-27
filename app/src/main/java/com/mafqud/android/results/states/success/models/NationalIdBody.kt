package com.mafqud.android.results.states.success.models
import androidx.annotation.Keep
import com.squareup.moshi.Json



@Keep
data class NationalIdBody(
    @field:Json(name = "national_id")
    val nationalId: String? = "" // 29907221202199
)