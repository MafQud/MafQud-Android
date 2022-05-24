package com.mafqud.android.locations
import androidx.annotation.Keep

import com.squareup.moshi.Json


class GovResponse : ArrayList<GovResponse.GovResponseItem>(){
    @Keep
    data class GovResponseItem(
        @Json(name = "id")
        val id: Int? = 0, // 27
        @Json(name = "name_ar")
        val nameAr: String? = "", // سوهاج
        @Json(name = "name_en")
        val nameEn: String? = "" // Sohag
    )
}