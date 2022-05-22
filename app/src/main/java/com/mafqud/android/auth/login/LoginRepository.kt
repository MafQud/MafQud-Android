package com.mafqud.android.auth.login

import com.mafqud.android.auth.login.models.LoginBody
import com.mafqud.android.auth.login.models.LoginResponse
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LoginRepository @Inject constructor() : BaseRepository() {

    suspend fun loginWithNumber(phoneLogin: LoginIntent.PhoneLogin): Result<Any> {
        return safeApiCall {
            val fcmToken = getUserFCMToken()
            return@safeApiCall coroutineScope {
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

    private suspend fun saveResultUserTokens(result: LoginResponse) {
        LogMe.i("accessToken", result.access)
        LogMe.i("refreshToken", result.refresh)
        saveUserTokens(accessToken = result.access, refreshToken = result.refresh)
    }


    //TODO request user data and save it
    suspend fun saveUser() {
        saveUserDataAndLogFlag()
    }
}