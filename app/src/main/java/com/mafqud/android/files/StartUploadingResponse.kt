package com.mafqud.android.files
import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class StartUploadingResponse(
    @field:Json(name = "fields")
    val fields: Fields? = Fields(),
    @field:Json(name = "id")
    val id: Int? = 0, // 10
    @field:Json(name = "url")
    val url: String? = "" // https://mafqud-bucket.s3.amazonaws.com/
) {
    @Keep
    data class Fields(
        @field:Json(name = "AWSAccessKeyId")
        val aWSAccessKeyId: String? = "", // AKIASLTWG66BJ6VD57XS
        @field:Json(name = "acl")
        val acl: String? = "", // public-read
        @field:Json(name = "Content-Type")
        val contentType: String? = "", // 00
        @field:Json(name = "key")
        val key: String? = "", // files/43c99c29429f4ac88e7bc002e65549b0.png
        @field:Json(name = "policy")
        val policy: String? = "", // eyJleHBpcmF0aW9uIjogIjIwMjItMDUtMjZUMDk6NTU6MThaIiwgImNvbmRpdGlvbnMiOiBbeyJhY2wiOiAicHVibGljLXJlYWQifSwgeyJDb250ZW50LVR5cGUiOiAiMDAifSwgWyJjb250ZW50LWxlbmd0aC1yYW5nZSIsIDEsIDEwNDg1NzYwXSwgeyJidWNrZXQiOiAibWFmcXVkLWJ1Y2tldCJ9LCB7ImtleSI6ICJmaWxlcy80M2M5OWMyOTQyOWY0YWM4OGU3YmMwMDJlNjU1NDliMC5wbmcifV19
        @field:Json(name = "signature")
        val signature: String? = "" // wT8ndGosd0JnEsoIvJwA+nsk7oo=
    )
}