package com.mafqud.android.locations
import androidx.annotation.Keep
import com.squareup.moshi.Json



@Keep
data class CitiesResponse(
    @field:Json(name = "results")
    val results: List<Result?>? = listOf()
) {
    @Keep
    data class Result(
        @field:Json(name = "id")
        val id: Int? = 0, // 56
        @field:Json(name = "name_ar")
        val nameAr: String? = "", // النزهة الجديدة
        @field:Json(name = "name_en")
        val nameEn: String? = "" // New Nozha
    )
}