package com.mafqud.android.base

import com.mafqud.android.di.MyServiceInterceptor
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.FCMManager
import com.mafqud.android.data.RemoteDataManager
import javax.inject.Inject

open class BaseRepository() {
    @Inject
    lateinit var remoteDataManager: RemoteDataManager

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var myServiceInterceptor: MyServiceInterceptor

    @Inject
    lateinit var fcmManager: FCMManager

    suspend fun getUserToken(): String {
        return dataStoreManager.readUserToken()
    }

  /*  *//**
     *  this fun to add two fields to data store->[local DB] (IS_LOGGED_IN, USER_TOKEN)
     *//*
    suspend fun saveUserDataAndLogFlag(
        user: AuthResponseSuccess.User,
    ) {
        // save user data to data store
        dataStoreManager.writeUserDataWithFlag(user)
        // update interceptor token header value
        myServiceInterceptor.setSessionToken(user.token)
    }

    *//**
     *  this fun to add two fields to data store->[local DB] (IS_LOGGED_IN, USER_TOKEN)
     *//*
    suspend fun saveUserData(
        user: AuthResponseSuccess.User,
    ) {
        // save user data to data store
        dataStoreManager.writeUserData(user)
        // update interceptor token header value
        myServiceInterceptor.setSessionToken(user.token)
    }


    *//**
     *
     *//*

    suspend fun getUserImageUrlFromDb(
    ): String {
        return dataStoreManager.readUserImage()
    }

    *//**
     *
     *//*

    suspend fun getUserData(
    ): AuthResponseSuccess.User {
        return dataStoreManager.readUserInfo()
    }

    *//**
     *
     *//*

    suspend fun getUserId(): Int {
        return dataStoreManager.getUserId()
    }


    *//**
     *
     *//*

    suspend fun isUserLoggedIn(
    ): Boolean {
        return dataStoreManager.isUserLoggedIn()
    }

    *//**
     *
     *//*
    suspend fun updateUserData(user: AuthResponseSuccess.User?) {
        user?.let {
            dataStoreManager.writeUserDataWithFlag(user)
        }
    }*/

    /**
     *  this fun to get user FCM token for notification
     */
    suspend fun getUserFCMToken() = fcmManager.getUserFCMToken()
}