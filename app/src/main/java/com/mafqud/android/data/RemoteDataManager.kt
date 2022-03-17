package com.mafqud.android.data

import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST


const val NO_AUTH_HEADER = "No-Auth"
const val AUTH_NOT_REQUIRED = "$NO_AUTH_HEADER: true"

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


    @POST("auth/social_login")
    @Headers(AUTH_NOT_REQUIRED)
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("firebase_token") firebaseToken: String,
    ): String


}
