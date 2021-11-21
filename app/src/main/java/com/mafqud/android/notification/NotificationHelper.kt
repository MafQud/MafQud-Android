package com.mafqud.android.notification

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
import com.mafqud.android.HomeActivity
import com.mafqud.android.R

class NotificationHelper private constructor(builder: Builder) {

    private lateinit var notification: Notification

    init {
        notify(builder.context,
                builder.title,
                builder.description,
                builder.max,
                builder.current,
                builder.isUpload,
                builder.type
        )
    }

    companion object {
        const val CHANNEL_ID_FCM_PUSH = "101"
        const val CHANNEL_ID_UPLOAD = "102"
        const val NOTIFICATION_FCM_PUSH = 101
        const val NOTIFICATION_UPLOAD = 102

    }

    private fun notify(
        context: Context,
        textTitle: String?,
        textContent: String?,
        max: Int,
        current: Int,
        uploadService: Boolean,
        type: Int
    ) {

        val intent =  Intent(context, HomeActivity::class.java)


        var notificationFlag = 0
        if (type == NOTIFICATION_FCM_PUSH) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            notificationFlag = PendingIntent.FLAG_UPDATE_CURRENT
            notificationChannelPush(context)

        } else if (type == NOTIFICATION_UPLOAD) {
            notificationFlag = 0
            notificationChannelUpload(context)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, notificationFlag)


        val builder = NotificationCompat.Builder(context, CHANNEL_ID_FCM_PUSH)
                .setSmallIcon(R.drawable.ic_flash_on)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_flash_off))
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle())
                .also {
                    if (max != Builder.MAX_EMPTY)
                        it.setProgress(max, current, false)
                }

        notification = builder.build()
        if (!uploadService) {
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(type, notification)
            }
        }

    }

    private fun notificationChannelUpload(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = context.getString(R.string.channel_name_upload)
            val descriptionText = context.getString(R.string.channel_description_upload)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_UPLOAD, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getNotificationObject(): Notification {
        return notification
    }


    private fun notificationChannelPush(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = context.getString(R.string.channel_name_push)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_FCM_PUSH, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    class Builder(val context: Context,
                  val isUpload: Boolean = false, ) {

        companion object {
            const val MAX_DEFAULT = 100
            const val MAX_EMPTY = -1

        }

        var type: Int = NOTIFICATION_FCM_PUSH
            private set

        var title: String? = context.getString(R.string.app_name)
            private set

        var description: String? = context.getString(R.string.app_name)
            private set

        var max: Int = MAX_EMPTY
            private set

        var current: Int = 0
            private set

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: String?): Builder {
            this.description = description
            return this
        }

        fun setType(type: Int): Builder {
            this.type = type
            return this
        }

        @JvmOverloads
        fun setProgress(max: Int = MAX_DEFAULT, current: Int = 0) {
            this.max = max
            this.current = current
        }

        fun build(): NotificationHelper {
            return NotificationHelper(this)
        }
    }
}