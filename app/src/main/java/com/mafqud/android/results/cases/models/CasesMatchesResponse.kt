package com.mafqud.android.results.cases.models
import androidx.annotation.Keep
import com.mafqud.android.home.model.CaseType
import com.squareup.moshi.Json
import java.io.Serializable


@Keep
data class CasesMatchesResponse(
    @field:Json(name = "count")
    val count: Int? = 0, // 1
    @field:Json(name = "limit")
    val limit: Int? = 0, // 10
    @field:Json(name = "next")
    val next: Any? = Any(), // null
    @field:Json(name = "offset")
    val offset: Int? = 0, // 0
    @field:Json(name = "previous")
    val previous: Any? = Any(), // null
    @field:Json(name = "results")
    val caseMatches: List<CaseMatch> = emptyList()
) {
    @Keep
    data class CaseMatch(
        @field:Json(name = "case")
        val case: Case? = Case(),
        @field:Json(name = "score")
        val score: Int? = 0 // 86
    ) : Serializable {
        @Keep
        data class Case(
            @field:Json(name = "id")
            val id: Int? = 0, // 5
            @field:Json(name = "last_seen")
            val lastSeen: String? = "", // null
            @field:Json(name = "location")
            val location: Location? = Location(),
            @field:Json(name = "name")
            val name: String? = "", // null
            @field:Json(name = "posted_at")
            val postedAt: Any? = Any(), // null
            @field:Json(name = "thumbnail")
            val thumbnail: String? = "", // https://mafqud-bucket.s3.amazonaws.com/media/files/77cf22de6a58432184972470f327ccc8.png
            @field:Json(name = "type")
            val type: String? = "" // F
        ) {
            fun getCaseType(): CaseType {
                return when(type){
                    "M" -> CaseType.MISSING
                    "F" -> CaseType.FOUND
                    else -> CaseType.NONE
                }
            }

            fun getFullAddress(): String {
                return location?.gov + " - " + location?.city
            }
            @Keep
            data class Location(
                @field:Json(name = "city")
                val city: String? = "", // الزاوية الحمراء
                @field:Json(name = "gov")
                val gov: String? = "" // القاهرة
            )
        }
    }
}
