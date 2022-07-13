package com.mafqud.android.auth.register

import com.mafqud.android.auth.login.models.LoginBody
import com.mafqud.android.auth.login.models.LoginResponse
import com.mafqud.android.auth.register.models.SignUpRequest
import com.mafqud.android.auth.register.models.ValidatePhoneRequest
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.other.getUserPayloadFromToken
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterRepository @Inject constructor() : BaseRepository() {

    suspend fun signUp(signupData: RegisterViewModel.SignUpForm): Result<Any> {
        return safeApiCall {
            val phoneWithoutZero = signupData.phone?.drop(1)
            val fcmToken = getUserFCMToken()
            return@safeApiCall coroutineScope {
                launch {
                    remoteDataManager.signUp(
                        SignUpRequest(
                            firebaseToken = fcmToken,
                            location = SignUpRequest.Location(
                                gov = signupData.govId,
                                city = signupData.cityId
                            ),
                            name = signupData.fullName,
                            username = phoneWithoutZero,
                            password = signupData.password
                        )
                    )
                    //delay(200)
                    val result = remoteDataManager.login(
                        LoginBody(
                            username = signupData.phone ?: "",
                            password = signupData.password ?: ""
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

    suspend fun isValidPhone(phoneIntent: RegisterIntent.ValidatePhone): Result<Unit> {
        return safeApiCall {
            val phoneWithoutZero = phoneIntent.phone.drop(1)
            remoteDataManager.validatePhone(
                ValidatePhoneRequest(phone = phoneWithoutZero)
            )
        }
    }
}