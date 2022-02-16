package com.mafqud.android.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
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
        const val USER_TOKEN = "userToken"
        const val USER_NAME = "userName"
        const val USER_ID = "userId"
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
    suspend fun writeUserTokenAndMarkAsLoggedIn(userToken: String) {
        mDataStore.edit { settings ->
            settings[booleanPreferencesKey(IS_LOGGED_IN)] = true
            settings[stringPreferencesKey(USER_TOKEN)] = userToken
        }
    }

    /**
     * for reading user token from datastore
     */
    suspend fun readUserToken(): String {
        /*val fakeToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvbGltZWVpbnQuY29tXC9iYXNlXC9wdWJsaWNcL2FwaVwvYXV0aFwvbG9naW4iLCJpYXQiOjE2NDE3MTAwODQsImV4cCI6MTY3MzI0NjA4NCwibmJmIjoxNjQxNzEwMDg0LCJqdGkiOiI3Mkt5Tno1MUgzdHJTYUJGIiwic3ViIjozLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.UbsWTkZCYWE1yNnOxLG7zPchi4-9iFvNIJYTeA46xpM"
        val token = if(BuildConfig.DEBUG) fakeToken else {
            mDataStore.data.map { settings ->
                settings[stringPreferencesKey(USER_TOKEN)] ?: ""
            }.first().toString()
        }*/
        val token =
            mDataStore.data.map { settings ->
                settings[stringPreferencesKey(USER_TOKEN)] ?: ""
            }.first().toString()
        return token
    }

    /**
     * for storing user info (name, image_url,...)
     */
    /*suspend fun writeUserDataWithFlag(user: AuthResponseSuccess.User) {
        val name = user.name
        val imageUrl = user.image
        val email = user.email
        val id = user.id
        val token = user.token
        val followers = user.followers ?: 0
        val following = user.following ?: 0

        mDataStore.edit { settings ->
            settings[stringPreferencesKey(USER_NAME)] = name
            settings[stringPreferencesKey(USER_TOKEN)] = token
            settings[stringPreferencesKey(USER_IMAGE)] = imageUrl
            settings[stringPreferencesKey(USER_EMAIL)] = email
            settings[intPreferencesKey(USER_ID)] = id
            settings[intPreferencesKey(USER_FOLLOWERS)] = followers
            settings[intPreferencesKey(USER_FOLLOWING)] = following
            settings[booleanPreferencesKey(IS_LOGGED_IN)] = true
        }
    }*/

    /**
     * for storing user info (name, image_url,...)
     */
    /*suspend fun writeUserData(user: AuthResponseSuccess.User) {
        val name = user.name
        val imageUrl = user.image
        val email = user.email
        val id = user.id
        val token = user.token
        val followers = user.followers ?: 0
        val following = user.following ?: 0

        mDataStore.edit { settings ->
            settings[stringPreferencesKey(USER_NAME)] = name
            settings[stringPreferencesKey(USER_TOKEN)] = token
            settings[stringPreferencesKey(USER_IMAGE)] = imageUrl
            settings[stringPreferencesKey(USER_EMAIL)] = email
            settings[intPreferencesKey(USER_ID)] = id
            settings[intPreferencesKey(USER_FOLLOWERS)] = followers
            settings[intPreferencesKey(USER_FOLLOWING)] = following
        }
    }
*/
    /**
     * for storing user info (name, image_url,...)
     */
    /*suspend fun readUserInfo(): AuthResponseSuccess.User {
        var name = ""
        var email = ""
        var id = 0
        var image = ""
        var token = ""
        var followers = 0
        var following = 0

        mDataStore.data.map { settings ->
            name = settings[stringPreferencesKey(USER_NAME)] ?: ""
            image = settings[stringPreferencesKey(USER_IMAGE)] ?: ""
            id = settings[intPreferencesKey(USER_ID)] ?: 0
            followers = settings[intPreferencesKey(USER_FOLLOWERS)] ?: 0
            following = settings[intPreferencesKey(USER_FOLLOWING)] ?: 0
            email = settings[stringPreferencesKey(USER_EMAIL)] ?: ""
            token = settings[stringPreferencesKey(USER_TOKEN)] ?: ""
        }.first().toString()
        return AuthResponseSuccess.User(
            email = email,
            id = id,
            image = image,
            name = name,
            token = token,
            followers = followers,
            following= following,
        )
    }*/

    /**
     * for storing user info image_url
     */
    suspend fun readUserImage(): String {
        val image = mDataStore.data.map { settings ->
            settings[stringPreferencesKey(USER_IMAGE)] ?: ""
        }.first().toString()
        return image
    }


    suspend fun getUserId(): Int {
        return mDataStore.data.map { settings ->
            settings[intPreferencesKey(USER_ID)] ?: 0
        }.first()
    }

    suspend fun isUserLoggedIn(): Boolean {
        val isLogged = read(IS_LOGGED_IN, false)
        val userToken = readUserToken()
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

    fun readTheme(key: String, defaultValue: Boolean): Flow<Boolean> {
        return mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(key)] ?: defaultValue
        }

    }

    suspend fun clearDataStore() {
        mDataStore.edit { it.clear() }
    }

}