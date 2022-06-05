package com.mafqud.android.data

import com.mafqud.android.auth.login.models.LoginBody
import com.mafqud.android.auth.login.models.LoginResponse
import com.mafqud.android.auth.register.models.SignUpRequest
import com.mafqud.android.auth.register.models.ValidatePhoneRequest
import com.mafqud.android.files.FinishUploadBody
import com.mafqud.android.files.FinishUploadingResponse
import com.mafqud.android.files.StartUploadBody
import com.mafqud.android.files.StartUploadingResponse
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.locations.CitiesResponse
import com.mafqud.android.locations.GovResponse
import com.mafqud.android.myAccountEdit.name.models.ChangeNameBody
import com.mafqud.android.notification.models.NotificationsResponse
import com.mafqud.android.report.uploading.models.CreateCaseBody
import com.mafqud.android.reportedCases.models.ReportedCasesResponse
import com.mafqud.android.results.caseDetails.models.CaseDetailsResponse
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.results.contact.models.CreateCaseContactBody
import com.mafqud.android.results.states.success.models.NationalIdBody
import com.mafqud.android.util.network.tokenRefresh.TokenRefreshBody
import com.mafqud.android.util.network.tokenRefresh.TokenRefreshResponse
import com.mafqud.android.util.network.tokenRefresh.TokenVerifyBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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


    @POST("/api/auth/phone/validate/")
    @Headers(AUTH_NOT_REQUIRED)
    suspend fun validatePhone(
        @Body validatePhoneRequest: ValidatePhoneRequest
    )

    @POST("/api/users/create/")
    @Headers(AUTH_NOT_REQUIRED)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    )

    @GET("/api/cases")
    suspend fun getCases(
        @Query("offset") offset: Int,
        @Query("type") type: String,
        @Query("limit") limit: Int,
        @Query("start_age") startAge: Int?,
        @Query("end_age") endAge: Int?,
        @Query("name") name: String?,
        @Query("gov") govID: Int?,
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
    suspend fun getReportedCases(
        @Query("offset") page: Int,
        @Query("limit") limit: Int,
    ): ReportedCasesResponse


    @GET("/api/notifications/")
    suspend fun getNotifications(
        @Query("offset") page: Int,
        @Query("limit") limit: Int,
    ): NotificationsResponse

    /**
     * location data
     */

    @GET("/api/locations/governorates/")
    suspend fun getGovs(): GovResponse

    @GET("/api/locations/governorates/{gov_id}/cities")
    suspend fun getCities(@Path("gov_id") govId: Int): CitiesResponse

    @POST("/api/files/upload/direct/start/")
    suspend fun startUploadingImage(
        @Body startUploadBody: StartUploadBody
    ): StartUploadingResponse

    @POST("/api/files/upload/direct/finish/")
    suspend fun finishUploadingImage(
        @Body finishUploadBody: FinishUploadBody
    ): FinishUploadingResponse


    @POST("/api/users/{userId}/set/id/")
    suspend fun setNationalId(
        @Path("userId") userId: Int,
        @Body nationalIdBody: NationalIdBody
    )

    @GET("/api/cases/{caseID}/publish")
    suspend fun publishCase(
        @Path("caseID") caseID: Int
    )

    @GET("/api/cases/{caseID}/matches")
    suspend fun getMatchesCases(
        @Path("caseID") caseID: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): CasesMatchesResponse

    @GET("/api/cases/{caseID}/")
    suspend fun getCaseDetails(
        @Path("caseID") caseID: Int,
    ): CaseDetailsResponse

    @GET("/api/cases/contacts/{caseID}/update/")
    suspend fun updateCaseContact(
        @Path("caseID") caseID: Int,
        // TODO
    ): CaseDetailsResponse

    @POST("/api/cases/contacts/create/")
    suspend fun createCaseContact(): CreateCaseContactBody


    @POST("/api/cases/create/")
    suspend fun uploadCase(@Body createCaseBody: CreateCaseBody)

    @GET("/api/notifications/{notificationID}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationID") id: Int
    )


    @POST("/api/users/{userID}/update/")
    suspend fun changeUserName(
        @Path("userID") userID: Int,
        @Body changeNameBody: ChangeNameBody
    )

}
