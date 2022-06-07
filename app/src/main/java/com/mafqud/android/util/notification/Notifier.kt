package com.mafqud.android.util.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mafqud.android.splash.SplashActivity
import com.mafqud.android.R


object Notifier {

    /**
     * Don't change these values
     */
    private const val CHANNEL_ID_PUSH = "101"
    private const val CHANNEL_ID_FCM = "102"
    const val NOTIFICATION_UPLOAD = 101
    private const val NOTIFICATION_FCM = 102


    private fun getPendingIntent(
        context: Context,
        intent: Intent
    ): PendingIntent {

        var flagType = PendingIntent.FLAG_ONE_SHOT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flagType = PendingIntent.FLAG_IMMUTABLE
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, flagType)

        return pendingIntent
    }

    private fun getCommonBuilder(
        context: Context,
        channelId: String,
        title: String = "",
        description: String = "",
        intent: Intent = Intent(context, SplashActivity::class.java),
        pendingIntent: PendingIntent = getPendingIntent(context, intent)
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_mafqud_logo)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_mafqud_logo
                )
            )
            .setColor(ContextCompat.getColor(context, R.color.blue))
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
    }

    fun fcmNotification(
        context: Context,
        title: String? = "",
        description: String? = ""
    ) {

        notificationChannelFCM(context)

        val builder = getCommonBuilder(
            context = context, channelId = CHANNEL_ID_FCM,
            title = title ?: "", description = description ?: ""
        )
        val notification = builder.build()

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            //cancelAll()
            notify(NOTIFICATION_FCM, notification)
        }

    }

    fun uploadNotification(
        context: Context,
        title: String = context.getString(R.string.uploading),
        description: String = ""
    ): Notification {

        notificationChannelUpload(context)

        val builder = getCommonBuilder(
            context = context, channelId = CHANNEL_ID_PUSH,
            title = title, description = description
        )
        val notification = builder.build()

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            //cancelAll()
            //notify(NOTIFICATION_UPLOAD, notification)
        }
        return notification
    }

    private fun notificationChannelUpload(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "Uploading image"
            val descriptionText = "This for uploading your images"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_PUSH, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun notificationChannelFCM(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "FCM notifications"
            val descriptionText = "This for pushing notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_FCM, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}