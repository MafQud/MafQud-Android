package com.mafqud.android.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mafqud.android.util.other.UserPayload
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton //You can ignore this annotation as return `datastore` from `preferencesDataStore` is singletone
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val mDataStore = appContext.dataStore


    companion object {
        /**
         * user info
         */
        const val USER_TOKEN_ACCESS = "userTokenAccess"
        const val USER_TOKEN_REFRESH = "userTokenRefresh"
        const val USER_NAME = "userName"
        const val USER_GOV = "userGov"
        const val USER_CITY = "userCity"
        const val USER_PHONE = "userPhone"
        const val USER_ID = "userId"
        const val USER_NATIONAL_ID = "userNationalId"
        const val USER_FCM_TOKEN = "fcmToken"
        const val USER_FOLLOWERS = "userFollowers"
        const val USER_FOLLOWING = "userFollowing"
        const val USER_IMAGE = "userImage"
        const val USER_EMAIL = "userEmail"

        /**
         * other
         */
        const val IS_PASSED_INTRO = "passedIntro"
        const val IS_LOGGED_IN = "loggedIn"
        const val LANGUAGE = "lang"
        const val IS_DARK = "isDark"
    }

    /**
     * for writing user token to datastore and mark user as logged in
     */
    /*suspend fun writeUserTokenAndMarkAsLoggedIn(userToken: String) {
        mDataStore.edit { settings ->
            settings[booleanPreferencesKey(IS_LOGGED_IN)] = true
            settings[stringPreferencesKey(USER_TOKEN)] = userToken
        }
    }*/

    /**
     * for reading user token from datastore
     */
    suspend fun readUserAccessToken(): String {
        val token =
            mDataStore.data.map { settings ->
                settings[stringPreferencesKey(USER_TOKEN_ACCESS)] ?: ""
            }.first().toString()
        return token
    }

    /**
     * for reading user token from datastore
     */
    suspend fun readUserRefreshToken(): String {
        val token =
            mDataStore.data.map { settings ->
                settings[stringPreferencesKey(USER_TOKEN_REFRESH)] ?: ""
            }.first().toString()
        return token
    }

    /**
     * for storing user info (name, image_url,...)
     */
    suspend fun writeUserDataWithFlag(user: UserPayload) {
        val name = user.name
        val id = user.userId
        val fcm = user.firebaseToken
        val nationalId = user.nationalId
        val phone = user.phone
        val gov = user.gov
        val city = user.city


        mDataStore.edit { settings ->
            settings[stringPreferencesKey(USER_NAME)] = name
            settings[stringPreferencesKey(USER_GOV)] = gov
            settings[stringPreferencesKey(USER_CITY)] = city
            settings[stringPreferencesKey(USER_PHONE)] = phone
            settings[intPreferencesKey(USER_ID)] = id
            settings[stringPreferencesKey(USER_NATIONAL_ID)] = nationalId
            settings[stringPreferencesKey(USER_FCM_TOKEN)] = fcm
            settings[booleanPreferencesKey(IS_LOGGED_IN)] = true
        }
    }
    /**
     * for storing user info (name, image_url,...)
     */
    suspend fun readUserData(): UserPayload {
        var name = ""
        var gov = ""
        var city = ""
        var phone = ""

        mDataStore.data.map { settings ->
            name = settings[stringPreferencesKey(USER_NAME)] ?: ""
            gov = settings[stringPreferencesKey(USER_GOV)] ?: ""
            city = settings[stringPreferencesKey(USER_CITY)] ?: ""
            phone = settings[stringPreferencesKey(USER_PHONE)] ?: ""
        }.first().toString()
        return UserPayload(
            name = name,
            gov = gov,
            city = city,
            phone = phone
        )
    }

    suspend fun getUserId(): Int {
        return mDataStore.data.map { settings ->
            settings[intPreferencesKey(USER_ID)] ?: 0
        }.first()
    }

    suspend fun isUserLoggedIn(): Boolean {
        val isLogged = read(IS_LOGGED_IN, false)
        val userToken = readUserAccessToken()
        return isLogged && userToken.trim().isNotEmpty()
    }

    suspend fun write(key: String, value: String) {
        mDataStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
        }
    }

    suspend fun read(key: String, defaultValue: String): String {
        return mDataStore.data.map { settings ->
            settings[stringPreferencesKey(key)] ?: defaultValue
        }.first().toString()
    }

    suspend fun write(key: String, value: Int) {
        mDataStore.edit { settings ->
            settings[intPreferencesKey(key)] = value
        }
    }

    suspend fun read(key: String, defaultValue: Int): Int {
        return mDataStore.data.map { settings ->
            settings[intPreferencesKey(key)] ?: defaultValue
        }.first().toInt()
    }

    suspend fun write(key: String, value: Double) {
        mDataStore.edit { settings ->
            settings[doublePreferencesKey(key)] = value
        }
    }

    suspend fun read(key: String, defaultValue: Double): Double {
        return mDataStore.data.map { settings ->
            settings[doublePreferencesKey(key)] ?: defaultValue
        }.first().toDouble()

    }

    suspend fun write(key: String, value: Boolean) {
        mDataStore.edit { settings ->
            settings[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun read(key: String, defaultValue: Boolean): Boolean {
        return mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(key)] ?: defaultValue
        }.first()

    }

    suspend fun isLoggedIn(): Boolean {
        val isLoggedIn = mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(IS_LOGGED_IN)] ?: false
        }.first()

        val isHasRefreshToken = mDataStore.data.map { settings ->
            settings[stringPreferencesKey(USER_TOKEN_REFRESH)] ?: ""
        }.first()

        val isHasAccessToken = mDataStore.data.map { settings ->
            settings[stringPreferencesKey(USER_TOKEN_REFRESH)] ?: ""
        }.first()
        return isLoggedIn && isHasRefreshToken.isNotEmpty() && isHasAccessToken.isNotEmpty()

    }

    fun readTheme(key: String, defaultValue: Boolean): Flow<Boolean> {
        return mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(key)] ?: defaultValue
        }

    }

    suspend fun clearDataStore() {
        mDataStore.edit { it.clear() }
    }

    suspend fun saveUserTokens(accessToken: String, refreshToken: String) {
        mDataStore.edit { settings ->
            settings[stringPreferencesKey(USER_TOKEN_ACCESS)] = accessToken
            settings[stringPreferencesKey(USER_TOKEN_REFRESH)] = refreshToken
        }
    }

    suspend fun saveUserAccessToken(accessToken: String) {
        mDataStore.edit { settings ->
            settings[stringPreferencesKey(USER_TOKEN_ACCESS)] = accessToken
        }
    }

    suspend fun getUserName(): String {
        return mDataStore.data.map { settings ->
            settings[stringPreferencesKey(USER_NAME)] ?: ""
        }.first()
    }

    suspend fun getUserAccessToken(): String {
        return mDataStore.data.map { settings ->
            settings[stringPreferencesKey(USER_TOKEN_ACCESS)] ?: ""
        }.first()
    }

    suspend fun getUserRefreshToken(): String {
        return mDataStore.data.map { settings ->
            settings[stringPreferencesKey(USER_TOKEN_REFRESH)] ?: ""
        }.first()
    }
}