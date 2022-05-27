package com.mafqud.android.notification.models

import androidx.annotation.Keep
import com.mafqud.android.home.model.CaseType
import com.squareup.moshi.Json


enum class NotificationIconType {
    SUCCESS,
    WARNING,
    INFO,
    ERROR,
    NONE
}

enum class NotificationAction {
    MATCHES,
    PUBLISHED,
    DETAILS,
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
        @field:Json(name = "action")
        val action: String? = "", // D
        @field:Json(name = "body")
        val body: String? = "", // جارى البحث عن المفقود وسنقوم بإشعارك فى حاله العثور لأى نتائج
        @field:Json(name = "case_id")
        val caseId: Int? = 0, // 4
        @field:Json(name = "case_type")
        val caseType: String? = "", // M
        @field:Json(name = "created_at")
        val createdAt: String? = "", // 2022-05-26T15:39:10.411464Z
        @field:Json(name = "id")
        val id: Int? = 0, // 7
        @field:Json(name = "level")
        val level: String? = "", // S
        @field:Json(name = "read_at")
        val readAt: String? = "", // 111
        var iconType: NotificationIconType = NotificationIconType.NONE // تم نشر الحاله بنجاح
    ) {
        fun getCaseType(): CaseType {
            return when(caseType){
                "M" -> CaseType.MISSING
                "F" -> CaseType.FOUND
                else -> CaseType.NONE
            }
        }

        fun getAction(): NotificationAction {
            return when(action){
                "M" -> NotificationAction.MATCHES
                "P" -> NotificationAction.PUBLISHED
                "D" -> NotificationAction.DETAILS
                "N" -> NotificationAction.NONE
                else -> NotificationAction.NONE
            }
        }
    }
    
}