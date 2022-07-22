package com.mafqud.android.auth.register.models
import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class ErrorSignUp(
    @field:Json(name = "detail")
    val detail: Detail? = Detail(),
    @field:Json(name = "message")
    val message: String? = "", // Validation error
    @field:Json(name = "status_code")
    val statusCode: Int? = 0 // 400
) {
    @Keep
    data class Detail(
        @field:Json(name = "firebase_token")
        val firebaseToken: List<String?>? = listOf(),
        @field:Json(name = "username")
        val username: List<String?>? = listOf()
    )
}