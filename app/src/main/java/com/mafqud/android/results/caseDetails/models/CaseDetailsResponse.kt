package com.mafqud.android.results.caseDetails.models
import androidx.annotation.Keep
import com.mafqud.android.home.model.CaseType
import com.squareup.moshi.Json
import java.io.Serializable


@Keep
data class CaseDetailsResponse(
    @field:Json(name = "details")
    val details: Details? = Details(),
    @field:Json(name = "location")
    val location: Location? = Location(),
    @field:Json(name = "photos")
    val photos: List<String?>? = listOf(),
    @field:Json(name = "state")
    val state: String? = "", // Active
    @field:Json(name = "type")
    val type: String? = "", // M
    @field:Json(name = "user")
    val user: String? = "" // 1111111111
) : Serializable {
    fun getCaseType(): CaseType {
        return when(type){
            "M" -> CaseType.MISSING
            "F" -> CaseType.FOUND
            else -> CaseType.NONE
        }
    }
    @Keep
    data class Details(
        @field:Json(name = "age")
        val age: Int? = 0, // 22
        @field:Json(name = "description")
        val description: String? = "", // Found him crying
        @field:Json(name = "gender")
        val gender: String? = "", // F
        @field:Json(name = "last_seen")
        val lastSeen: String? = "", // 2021-04-29
        @field:Json(name = "location")
        val location: Location? = Location(),
        @field:Json(name = "name")
        val name: String? = "" // omar
    ) : Serializable {
        @Keep
        data class Location(
            @field:Json(name = "address")
            val address: String? = "", // Hy El-Gamaa
            @field:Json(name = "city")
            val city: String? = "", // El-Zaytoun
            @field:Json(name = "gov")
            val gov: String? = "", // Cairo
            @field:Json(name = "lat")
            val lat: String? = "", // 31.362806
            @field:Json(name = "lon")
            val lon: String? = "" // 31.031443
        ) {
            fun getFullAddress(): String {
                return "${gov ?: ""} - ${city ?: ""}"
            }
        }
    }

    @Keep
    data class Location(
        @field:Json(name = "address")
        val address: String? = "", // Hy El-Gamaa
        @field:Json(name = "city")
        val city: String? = "", // El-Zaytoun
        @field:Json(name = "gov")
        val gov: String? = "", // Cairo
        @field:Json(name = "lat")
        val lat: String? = "", // 31.362806
        @field:Json(name = "lon")
        val lon: String? = "" // 31.031443
    )  : Serializable {
        fun getFullAddress(): String {
            return "${gov ?: ""} - ${city ?: ""}"
        }
    }
}