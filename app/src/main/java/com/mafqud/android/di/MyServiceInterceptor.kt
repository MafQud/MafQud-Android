package com.mafqud.android.di

import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.NO_AUTH_HEADER
import com.mafqud.android.data.REFRESH_HEADER
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.util.network.tokenRefresh.TokenRefreshBody
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val BEARER = "Bearer"

@Singleton
class MyServiceInterceptor @Inject constructor(
    dataStoreManager: DataStoreManager,
) : Interceptor {
    var remoteDataManager: RemoteDataManager? = null

    var sessionAccessToken: String = ""
    var sessionRefreshToken: String = ""

    init {
        runBlocking {
            sessionAccessToken = dataStoreManager.readUserAccessToken()
            sessionRefreshToken = dataStoreManager.readUserRefreshToken()
        }
    }

    fun setSessionToken(sessionToken: String) {
        this.sessionAccessToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        // @required  -> (Accept) header
        //requestBuilder.addHeader("Accept", "application/json")

        // adding Bearer token as needed
        val bearer = BEARER
        if (request.header(NO_AUTH_HEADER) == null) {
            LogMe.i("AuthorizationHeader", "is Added")
            if (sessionAccessToken.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "$bearer $sessionAccessToken")
            }
        } else {
            LogMe.i("AuthorizationHeader", "Not Added")
        }
        return chain.proceed(requestBuilder.build())
    }
}

/*
 val requestBody =
            "Not authorized user".toResponseBody("plain/text".toMediaTypeOrNull())

        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        // adding Bearer token as needed
        val bearer = BEARER
        if (request.header(NO_AUTH_HEADER) == null) {
            LogMe.i("AuthorizationHeader", "is Added")
            if (sessionAccessToken.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "$bearer $sessionAccessToken")
            }
        } else {
            LogMe.i("AuthorizationHeader", "Not Added")
        }

        // try the request
        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 && request.header(REFRESH_HEADER) != null) {
            response.close()
            throw IOException()
        }


        if (response.code == 200 && request.header(REFRESH_HEADER) != null) {

        }

        if (response.code == 401 && request.header(REFRESH_HEADER) == null) {
            // close previous response
            response.close()

            // get a new token (I use a synchronous Retrofit call)
            val newToken = remoteDataManager?.refreshAccessToken(
                TokenRefreshBody(
                    refresh = sessionRefreshToken
                )
            )?.execute()?.body()?.access!!

            sessionAccessToken = newToken

            // create a new request and modify it accordingly using the new token
            val newRequest = request.newBuilder().removeHeader("Authorization")

            if (request.header(NO_AUTH_HEADER) == null) {
                if (sessionAccessToken.isNotEmpty()) {
                    newRequest.addHeader("Authorization", "$bearer $sessionAccessToken")
                }
            } else {

            }

            // retry the request
            return chain.proceed(newRequest.build());
        }
        return chain.proceed(requestBuilder.build())
 */