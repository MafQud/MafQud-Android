package com.mafqud.android.auth.login

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.delay
import javax.inject.Inject

class LoginRepository @Inject constructor() : BaseRepository() {

    suspend fun loginWithNumber(phoneLogin: LoginIntent.PhoneLogin): Result<String> {
        return safeApiCall {
            val fcmToken = getUserFCMToken()
            /*remoteDataManager.login(
                username = phoneLogin.phone,
                password = phoneLogin.password,
                firebaseToken = fcmToken
            )*/
            // TODO "" be removed
            delay(2000)
            ""
        }
    }


    //TODO request user data and save it
    suspend fun saveUser() {
        saveUserDataAndLogFlag()
    }
}