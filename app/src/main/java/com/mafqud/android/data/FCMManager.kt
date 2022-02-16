package com.mafqud.android.data

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMManager @Inject constructor() {

    /*suspend fun getUserFCMToken(): String {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("getUserFCMToken: ", "done")

            } catch (e: Exception) {
                Log.i("getUserFCMToken: ", e.localizedMessage.toString())
                ""
            }
        }
    }*/

    suspend fun getUserFCMToken(): String {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            // handle error
            ""
        }
    }
}