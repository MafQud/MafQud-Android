package com.mafqud.android.notification.models

import androidx.annotation.Keep
import com.squareup.moshi.Json


enum class NotificationIconType {
    SUCCESS,
    WARNING,
    INFO,
    ERROR,
    NONE
}

@Keep
data class NotificationsResponse(
    @field:Json(name = "count")
    val count: Int? = 0, // 2
    @field:Json(name = "limit")
    val limit: Int? = 0, // 10
    @field:Json(name = "next")
    val next: Any? = Any(), // null
    @field:Json(name = "offset")
    val offset: Int? = 0, // 0
    @field:Json(name = "previous")
    val previous: Any? = Any(), // null
    @field:Json(name = "results")
    val notifications: List<Notification> = emptyList()
) {
    @Keep
    data class Notification(
        @field:Json(name = "body")
        val body: String? = "", // تم نشر بيانات المعثور عليه بنجاح انتظر منا اشعار اخر فى حين الوصول لأى نتائج
        @field:Json(name = "created_at")
        val createdAt: String? = "", // 2022-05-23T10:58:34.835169Z
        @field:Json(name = "id")
        val id: Int? = 0, // 11
        @field:Json(name = "level")
        val level: String? = "", // S
        @field:Json(name = "read_at")
        val readAt: Any? = Any(), // null
        @field:Json(name = "sent_to")
        val sentTo: String? = "", // /api/users/3/
        @field:Json(name = "title")
        val title: String? = "",// تم نشر الحاله بنجاح
        var iconType: NotificationIconType = NotificationIconType.NONE // تم نشر الحاله بنجاح

    )
}