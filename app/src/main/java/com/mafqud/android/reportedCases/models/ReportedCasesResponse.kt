package com.mafqud.android.reportedCases.models

import androidx.annotation.Keep

import com.squareup.moshi.Json


@Keep
data class ReportedCasesResponseItem(
    @Json(name = "id")
    val id: Int? = 0, // 18
    @Json(name = "last_seen")
    val lastSeen: String? = "", // 2021-04-29
    @Json(name = "location")
    val location: Location? = Location(),
    @Json(name = "name")
    val name: String? = "", // omaddr
    @Json(name = "posted_at")
    val postedAt: String? = "", // 2022-05-23T10:58:34.833447Z
    @Json(name = "state")
    val state: String? = "", // Active
    @Json(name = "thumbnail")
    val thumbnail: String? = "", // https://mafqud-bucket.s3.amazonaws.com/media/files/04af7b80a24640ecb58ee0a6be536772.png
    @Json(name = "type")
    val type: String? = "" // F
) {
    @Keep
    data class Location(
        @Json(name = "city")
        val city: String? = "", // الزيتون
        @Json(name = "gov")
        val gov: String? = "" // القاهرة
    )
}