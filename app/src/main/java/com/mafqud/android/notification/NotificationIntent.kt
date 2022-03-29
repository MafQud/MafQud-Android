package com.mafqud.android.notification

sealed class NotificationIntent {
    object GetAllData : NotificationIntent()
    object RefreshData : NotificationIntent()
}