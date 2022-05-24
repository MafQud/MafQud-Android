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
        val currentAccessToken = myServiceInterceptor.sessionAccessToken

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
            //TODO you can fake that refresh token is invalid
            // (currentRefreshToken + "1")
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

        } else return null
    }

}
