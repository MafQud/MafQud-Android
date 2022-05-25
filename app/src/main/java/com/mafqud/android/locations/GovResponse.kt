package com.mafqud.android.locations
import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class GovResponse(
    @field:Json(name = "results")
    val results: List<Result?>? = listOf()
) {
    @Keep
    data class Result(
        @field:Json(name = "id")
        val id: Int? = 0, // 1
        @field:Json(name = "name_ar")
        val nameAr: String? = "", // القاهرة
        @field:Json(name = "name_en")
        val nameEn: String? = "" // Cairo
    )
}
