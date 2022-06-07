package com.mafqud.android.report.uploading.models

import androidx.annotation.Keep
import com.mafqud.android.home.model.CaseType
import com.squareup.moshi.Json
import java.io.Serializable


@Keep
data class CreateCaseBody(
    @field:Json(name = "details")
    val details: Details? = null,
    @field:Json(name = "file_ids")
    var fileIds: List<Int?>? = null,
    @field:Json(name = "location")
    var location: Location? = null,
    @field:Json(name = "thumbnail")
    var thumbnail: Int? = null, // 1
    @field:Json(name = "type")
    var type: String? = null, // M
    @Transient
    var caseType: CaseType = CaseType.NONE
) : Serializable {

    @Keep
    data class Details(
        @field:Json(name = "age")
        val age: Int? = 0, // 22
        @field:Json(name = "description")
        val description: String? = "", // Found him crying
        @field:Json(name = "gender")
        val gender: String? = "", // F
        @field:Json(name = "last_seen")
        val lastSeen: String? = "", // 2021-4-29
        @field:Json(name = "location")
        val location: Location? = Location(),
        @field:Json(name = "name")
        val name: String? = "" // omaddr
    ) : Serializable {
        @Keep
        data class Location(
            @field:Json(name = "address")
            val address: String? = "", // Hy El-Gamaa
            @field:Json(name = "city")
            val city: String? = "", // 9
            @field:Json(name = "gov")
            val gov: String? = "", // 1
            @field:Json(name = "lat")
            val lat: Double? = 0.0, // 31.362806
            @field:Json(name = "lon")
            val lon: Double? = 0.0 // 31.031443
        )
    }

    @Keep
    data class Location(
        @field:Json(name = "address")
        val address: String? = null, // Hy El-Gamaa
        @field:Json(name = "city")
        val city: String? = "", // 9
        @field:Json(name = "gov")
        val gov: String? = "", // 1
        @field:Json(name = "lat")
        val lat: Double? = null, // 31.362806
        @field:Json(name = "lon")
        val lon: Double? = null // 31.031443
    ) : Serializable
}
