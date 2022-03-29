package com.mafqud.android.notification

data class NotificationResponse(
    val id: Int = 0,
    val data: List<Data> = emptyList()

) {
    data class Data(
        val id: Int = 0,
        val message: String = "",
    )
}
