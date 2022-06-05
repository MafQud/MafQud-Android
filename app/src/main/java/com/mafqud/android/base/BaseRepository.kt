package com.mafqud.android.base

import android.app.Application
import android.content.Context
import com.mafqud.android.base.activity.BaseActivity
import com.mafqud.android.data.AWSUploading
import com.mafqud.android.data.DataStoreManager
import com.mafqud.android.data.FCMManager
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.di.MyServiceInterceptor
import com.mafqud.android.util.other.UserPayload
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class BaseRepository() {
    @Inject
    lateinit var remoteDataManager: RemoteDataManager

    @Inject
    lateinit var awsUploading: AWSUploading

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var myServiceInterceptor: MyServiceInterceptor

    @Inject
    @ApplicationContext
    lateinit var  appContext: Context

    @Inject
    lateinit var fcmManager: FCMManager

    suspend fun getUserToken(): String {
        return dataStoreManager.readUserAccessToken()
    }

    suspend fun getUserID(): Int {
        return dataStoreManager.getUserId()
    }

    suspend fun getUserName(): String {
        return dataStoreManager.getUserName()
    }

    suspend fun setUserName(userName: String) {
        return dataStoreManager.saveUserName(userName)
    }
    suspend fun getDisplayUserName(): String {
        return dataStoreManager.getUserName()
    }

    /*  */
    /**
     *  this fun to add two fields to data store->[local DB] (IS_LOGGED_IN, USER_TOKEN)
     */

    suspend fun saveUserDataAndLogFlag(user: UserPayload, accessToken: String) {
        // save user data to data store
        dataStoreManager.writeUserDataWithFlag(user)
        // update interceptor token header value
        myServiceInterceptor.setSessionToken(accessToken)
    }

    suspend fun saveUserTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        // save user data to data store
        dataStoreManager.saveUserTokens(accessToken, refreshToken)
        // update interceptor token header value
        myServiceInterceptor.setSessionToken(accessToken)
    }

    /**
     *  this fun to get user FCM token for notification
     */
    suspend fun getUserFCMToken() = fcmManager.getUserFCMToken()

    /**
     * location data
     */
    suspend fun getGos() = remoteDataManager.getGovs()

    suspend fun getCities(govId: Int) = remoteDataManager.getCities(govId = govId)

    /**
     * current app language type
     */

    suspend fun getAppLanguage() = dataStoreManager.read(
        DataStoreManager.LANGUAGE,
        BaseActivity.LangType.AR.toString()
    )


    suspend fun getNationalID() = dataStoreManager.getNationalId()

    suspend fun setNationalID(nationalId :String) = dataStoreManager.saveUserNationalID(nationalId)
}