package com.mafqud.android.notification

sealed class NotificationIntent {
    object GetAllData : NotificationIntent()
    data class MarkNotificationAsRead(val id: Int) : NotificationIntent()
    object RefreshData : NotificationIntent()
}