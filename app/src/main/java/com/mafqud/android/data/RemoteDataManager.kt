package com.mafqud.android.data

import com.mafqud.android.auth.login.models.LoginBody
import com.mafqud.android.auth.login.models.LoginResponse
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.notification.NotificationResponse
import com.mafqud.android.notification.models.NotificationsResponse
import com.mafqud.android.reportedCases.models.ReportedCasesResponseItem
import com.mafqud.android.util.network.tokenRefresh.TokenRefreshBody
import com.mafqud.android.util.network.tokenRefresh.TokenRefreshResponse
import com.mafqud.android.util.network.tokenRefresh.TokenVerifyBody
import retrofit2.Call
import retrofit2.http.*


const val NO_AUTH_HEADER = "No-Auth"
const val REFRESH_HEADER = "No-Refresh"
const val AUTH_NOT_REQUIRED = "$NO_AUTH_HEADER: true"
const val REFRESH_TOKEN = "$REFRESH_HEADER: true"

interface RemoteDataManager {

    /*  */
    /**
     * add NO-Auth header for endpoints the don't need any auth header (Bearer)
     *//*
    @POST("auth/social_register")
    @Headers(AUTH_NOT_REQUIRED)
    suspend fun socialRegister(@Body socialRequest: SocialRequest): AuthResponseSuccess

    */
    /**
     * All coming endpoints need (Bearer token) header
     *//*

    @GET("auth/profile")
    suspend fun getUserProfile(): AuthResponseSuccess


    @POST("auth/update_password")
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
    ): GeneralResponse*/


    @POST("api/auth/token/")
    @Headers(AUTH_NOT_REQUIRED)
    suspend fun login(
        @Body loginBody: LoginBody
    ): LoginResponse


    @GET("/api/cases")
    suspend fun getCases(
        @Query("offset") page: Int,
        @Query("type") type: String,
        @Query("limit") limit: Int,
    ): CasesDataResponse

    @POST("/api/auth/token/refresh/")
    @Headers(AUTH_NOT_REQUIRED, REFRESH_TOKEN)
    fun refreshAccessToken(
       @Body refreshToken: TokenRefreshBody
    ): Call<TokenRefreshResponse>

    @POST("/api/auth/token/verify/")
    @Headers(AUTH_NOT_REQUIRED)
    suspend fun verifyAccessToken(
        @Body verifyBody: TokenVerifyBody
    ): Any

    @GET("/api/users/cases")
    // TODO change response model
    suspend fun getReportedCases(
        @Query("offset") page: Int,
        @Query("limit") limit: Int,
    ): ReportedCasesResponseItem


    @GET("/api/notifications/")
    suspend fun getNotifications(
        @Query("offset") page: Int,
    ): NotificationsResponse


    @GET("/api/locations/governorates/")
    suspend fun getGovs(): NotificationsResponse

}
