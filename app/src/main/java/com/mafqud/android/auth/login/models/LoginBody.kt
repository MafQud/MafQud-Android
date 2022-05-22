package com.mafqud.android.auth.login.models
import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class LoginBody(
    @field:Json(name = "password")
    val password: String = "", // HardToGuessPasswordOMG!
    @field:Json(name = "username")
    val username: String = "" // 1005469972
)