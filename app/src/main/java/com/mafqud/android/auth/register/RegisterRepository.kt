package com.mafqud.android.auth.register

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.delay
import javax.inject.Inject

class RegisterRepository @Inject constructor() : BaseRepository() {

    suspend fun signUp(signupData: RegisterIntent.Signup): Result<String> {
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


    suspend fun getGovAndCity(): Result<String> {
        return safeApiCall {
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



    suspend fun isValidEmail(email: RegisterIntent.ValidateEmail): Result<String> {
        return safeApiCall {
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

    suspend fun isValidPhone(phone: RegisterIntent.ValidatePhone): Result<String> {
        return safeApiCall {
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
        //saveUserDataAndLogFlag(user)
    }
}