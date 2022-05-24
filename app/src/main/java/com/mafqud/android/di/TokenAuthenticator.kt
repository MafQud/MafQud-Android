package com.mafqud.android.di

import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.REFRESH_HEADER
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.util.network.tokenRefresh.TokenRefreshBody
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.Route
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenAuthenticator @Inject constructor() : Authenticator {

    var remoteDataManager: RemoteDataManager? = null

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var myServiceInterceptor: MyServiceInterceptor

    override fun authenticate(route: Route?, response: Response): Request? {
        val requestBody =
            "Not authorized user".toResponseBody("plain/text".toMediaTypeOrNull())

        val currentAccessToken = myServiceInterceptor.sessionAccessToken

        var body: Request? = null
        if (response.request.header(REFRESH_HEADER) != null && response.code == 401)
            return null

        if (response.request.header(REFRESH_HEADER) != null && response.code == 200)
            return null

        if (remoteDataManager != null && response.request.header("Authorization") != null) {

            val bearer = BEARER
            // Refresh your access_token using a synchronous api request
            val currentRefreshToken = myServiceInterceptor.sessionRefreshToken
            val newToken = myServiceInterceptor.sessionAccessToken
            if (newToken != currentAccessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "$bearer $newToken")
                    .build()
            }

            //try refresh token
            val newAccessTokenCall = remoteDataManager?.refreshAccessToken(
                TokenRefreshBody(refresh = currentRefreshToken)
            )
            LogMe.i("newAccessTokenCall", "newAccessTokenCall")

            try {
                val updatedToken = newAccessTokenCall!!.execute().body()!!.access!!
                runBlocking {
                    dataStoreManager.saveUserAccessToken(updatedToken)
                    myServiceInterceptor.setSessionToken(updatedToken)
                }
                return response.request.newBuilder()
                    .header("Authorization", "$bearer $updatedToken")
                    .build()
            } catch (E: Exception) {
                return null
            }
            /*when (newAccessTokenCall) {
                is Result.NetworkError.Generic -> {
                    LogMe.i("TokenAuthennewToken", "newToken")
                    //body = HttpException(retrofit2.Response.error<String>(401, requestBody))

                }
                Result.NetworkError.NoInternet -> {
                    LogMe.i("TokenAuthennewToken", "newToken")
                    //body = HttpException(retrofit2.Response.error<String>(401, requestBody))
                }
                is Result.Success -> {
                    val newToken = newAccessTokenCall.data?.access ?: throw HttpException(
                        retrofit2.Response.error<String>(
                            401,
                            requestBody
                        )
                    )
                    LogMe.i("TokenAuthennewToken", newToken)
                    myServiceInterceptor.setSessionToken(newToken)
                    // Add new header to rejected request and retry it
                    body = response.request.newBuilder()
                        .header("Authorization", "$bearer $newToken")
                        .build()
                }
            }*/

        } else return null
        //return body
    }

}
