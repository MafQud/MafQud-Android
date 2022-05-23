package com.mafqud.android.util.other

import androidx.annotation.Keep
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT
import com.squareup.moshi.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

@Keep
data class UserPayload(
    @field:Json(name = "city")
    val city: String? = "", // البساتين
    @field:Json(name = "exp")
    val exp: Int? = 0, // 1653333848
    @field:Json(name = "firebase_token")
    val firebaseToken: String = "", // sadtoken
    @field:Json(name = "gov")
    val gov: String? = "", // القاهرة
    @field:Json(name = "iat")
    val iat: Int? = 0, // 1653247448
    @field:Json(name = "jti")
    val jti: String? = "", // 6c6d22abba9645bda8ff255a0396682b
    @field:Json(name = "name")
    val name: String = "", // Osama Yasser Saber
    @field:Json(name = "national_id")
    val nationalId: String = "", // null
    @field:Json(name = "phone")
    val phone: String = "", // 1005469972
    @field:Json(name = "token_type")
    val tokenType: String? = "", // refresh
    @field:Json(name = "user_id")
    val userId: Int = -1 // 2
)

fun getUserPayloadFromToken(accessToken: String): UserPayload {
    try {
        val jwt = JWT(accessToken)
        val name = jwt.getClaim("name").asString() ?: ""
        val id = jwt.getClaim("user_id").asInt() ?: -1
        val phone = jwt.getClaim("phone").asString() ?: ""
        val firebaseToken = jwt.getClaim("firebase_token").asString() ?: ""
        val nationalId = jwt.getClaim("national_id").asString() ?: ""

        LogMe.i("user", name)
        LogMe.i("user", id.toString())
        LogMe.i("user", phone)

        return UserPayload(
            userId = id,
            name = name,
            phone = phone,
            firebaseToken = firebaseToken,
            nationalId = nationalId
        )

    } catch (e: DecodeException) {
        val requestBody =
            "Cannot parse user payload".toResponseBody("plain/text".toMediaTypeOrNull())
        // 422 -> HttpErrorType.DataInvalid
        throw HttpException(Response.error<String>(422, requestBody))
    }
}