package com.mafqud.android.home.model
import androidx.annotation.Keep
import com.squareup.moshi.Json


enum class CaseType{
    FOUND,
    MISSING,
    NONE
}

@Keep
data class CasesDataResponse(
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
    val cases: List<Case> = emptyList()
) {
    @Keep
    data class Case(
        @field:Json(name = "id")
        val id: Int? = 0, // 1
        @field:Json(name = "last_seen")
        val lastSeen: String? = "", // 2021-04-29
        @field:Json(name = "location")
        val location: Location? = Location(),
        @field:Json(name = "name")
        val name: String? = "", // omaddr
        @field:Json(name = "posted_at")
        val postedAt: String? = "", // 2022-05-19T19:59:02.064755Z
        @field:Json(name = "thumbnail")
        val thumbnail: String? = "", // https://mafqud-bucket.s3.amazonaws.com/media/files/b366332189494525abe04591a583544f.png
        @field:Json(name = "type")
        val type: String? = "" ,// F
        /*val mCaseType: CaseType =CaseType.NONE*/

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
            val city: String? = "", // الزيتون
            @field:Json(name = "gov")
            val gov: String? = "" // القاهرة
        )
    }
}

