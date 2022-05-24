package com.mafqud.android.notification

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.mafqud.android.notification.models.NotificationIconType
import com.mafqud.android.notification.models.NotificationsResponse
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {


    suspend fun getNotifications(): Flow<PagingData<NotificationsResponse.Notification>>? {
        val notificationResponse = notificationRepository.getNotifications()
        return when (notificationResponse) {
            is Result.Success -> returnNotifications(notificationResponse.data)
            else -> null
        }
    }

    private fun returnNotifications(data: Pager<Int, NotificationsResponse.Notification>):
            Flow<PagingData<NotificationsResponse.Notification>> {
        return data.flow.map {
            it.map {
                val notificationType = when (it.level) {
                    "S" -> NotificationIconType.SUCCESS
                    "E" -> NotificationIconType.ERROR
                    "I" -> NotificationIconType.INFO
                    "W" -> NotificationIconType.WARNING
                    else -> NotificationIconType.NONE
                }
                it.iconType = notificationType
                return@map it
            }
        }
    }
}