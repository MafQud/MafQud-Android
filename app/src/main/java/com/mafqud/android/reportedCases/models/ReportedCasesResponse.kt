package com.mafqud.android.reportedCases.models

import androidx.annotation.Keep
import com.mafqud.android.home.model.CaseType
import com.squareup.moshi.Json

enum class UserCaseState{
    ACTIVE,
    MISSING,
    NONE
}

@Keep
data class ReportedCasesResponse(
    @field:Json(name = "count")
    val count: Int? = 0, // 1
    @field:Json(name = "next")
    val next: Any? = Any(), // null
    @field:Json(name = "previous")
    val previous: Any? = Any(), // null
    @field:Json(name = "results")
    val userCases: List<UserCase> = emptyList()
) {
    @Keep
    data class UserCase(
        @field:Json(name = "id")
        val id: Int? = 0, // 18
        @field:Json(name = "last_seen")
        val lastSeen: String? = "", // 2021-04-29
        @field:Json(name = "location")
        val location: Location? = Location(),
        @field:Json(name = "name")
        val name: String? = "", // omaddr
        @field:Json(name = "posted_at")
        val postedAt: String? = "", // 2022-05-23T10:58:34.833447Z
        @field:Json(name = "state")
        val state: String? = "", // Active
        @field:Json(name = "thumbnail")
        val thumbnail: String? = "", // https://mafqud-bucket.s3.amazonaws.com/media/files/04af7b80a24640ecb58ee0a6be536772.png
        @field:Json(name = "type")
        val type: String? = "" // F
    ) {

        fun getCaseState(): UserCaseState {
            //TODO map it
            return when(state){
                "Active" -> UserCaseState.ACTIVE
                "FF" -> UserCaseState.MISSING
                else -> UserCaseState.NONE
            }
        }

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