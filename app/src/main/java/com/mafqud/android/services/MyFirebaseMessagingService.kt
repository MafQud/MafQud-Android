package com.mafqud.android.services


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mafqud.android.util.notification.NotificationHelper
import com.mafqud.android.util.notification.NotificationHelper.Companion.NOTIFICATION_FCM_PUSH
import com.mafqud.android.util.other.Logger


class MyFirebaseMessagingService  : FirebaseMessagingService() {

    /**
     * To receive notifications in foregrounded apps,
     * to receive data payload,
     * to send upstream messages,
     * and so on, you must extend this service.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        pushFCMNotification(remoteMessage)
    }

    private fun pushFCMNotification(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Logger.e("Message Notification Body:", it.body!!)
            val build = NotificationHelper.Builder(context = baseContext)
                .setTitle(it.title)
                .setDescription(it.body)
                .setType(NOTIFICATION_FCM_PUSH)
                .build()


        }
    }

    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private fun sendRegistrationToServer(token: String) {
        // TODO
    }

}