package com.mafqud.android.auth.register.models
import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class SignUpRequest(
    @field:Json(name = "firebase_token")
    val firebaseToken: String? = "", // sadtoken//
    @field:Json(name = "location")
    val location: Location? = Location(),
    @field:Json(name = "name")
    val name: String? = "", // Ahmed shehata
    @field:Json(name = "password")
    val password: String? = "", // 1111111111//
    @field:Json(name = "username")
    val username: String? = "" // 1111111111
) {
    @Keep
    data class Location(
        @field:Json(name = "city")
        val city: Int? = 0, // 1
        @field:Json(name = "gov")
        val gov: Int? = 0 // 1
    )
}