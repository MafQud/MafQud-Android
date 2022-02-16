package com.mafqud.android.di

import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.NO_AUTH_HEADER
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

const val BEARER = "Bearer"

@Singleton
class MyServiceInterceptor @Inject constructor(dataStoreManager: DataStoreManager) : Interceptor {

    private var sessionToken: String = ""

    init {
        runBlocking {
            sessionToken = dataStoreManager.readUserToken()
        }
    }

    fun setSessionToken(sessionToken: String) {
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        // @required  -> (Accept) header
        requestBuilder.addHeader("Accept", "application/json")

        // adding Bearer token as needed
        val bearer = BEARER
        if (request.header(NO_AUTH_HEADER) == null) {
            if (sessionToken.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "$bearer $sessionToken")
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}