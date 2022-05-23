package com.mafqud.android.auth.login

import com.mafqud.android.auth.login.models.LoginBody
import com.mafqud.android.auth.login.models.LoginResponse
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import com.mafqud.android.util.other.getUserPayloadFromToken
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class LoginRepository @Inject constructor() : BaseRepository() {

    suspend fun loginWithNumber(phoneLogin: LoginIntent.PhoneLogin): Result<Any> {
        return safeApiCall {
            val fcmToken = getUserFCMToken()
            return@safeApiCall coroutineScope {
                launch {
                    val result = remoteDataManager.login(
                        LoginBody(
                            username = phoneLogin.phone,
                            password = phoneLogin.password
                        )
                    )
                    saveResultUserTokens(result)
                }
            }
        }
    }

    private suspend fun saveResultUserTokens(result: LoginResponse) {
        LogMe.i("accessToken", result.access)
        LogMe.i("refreshToken", result.refresh)
        saveUserTokens(accessToken = result.access, refreshToken = result.refresh)
        saveUser(accessToken = result.access)
    }

    private suspend fun saveUser(accessToken: String) {
        val user = getUserPayloadFromToken(accessToken)
        saveUserDataAndLogFlag(user = user, accessToken = accessToken)

    }


}