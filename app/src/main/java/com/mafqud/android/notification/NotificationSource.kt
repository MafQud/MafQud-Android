package com.mafqud.android.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.notification.models.NotificationsResponse
import com.mafqud.android.util.other.LogMe

const val INITIAL_OFFSET = 0
const val PAGE_LIMIT_AND_OFFSET = 10

class NotificationSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, NotificationsResponse.Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationsResponse.Notification> {
        return try {
            val currentOffset = params.key ?: INITIAL_OFFSET
            LogMe.i("OffsetNotificationSource", currentOffset.toString())
            val notificationsResponse = remoteData.getNotifications(
                page = currentOffset,
                limit = PAGE_LIMIT_AND_OFFSET
            )

            val nextPage: Int? =
                if (notificationsResponse.notifications.isEmpty()) null else currentOffset + PAGE_LIMIT_AND_OFFSET
            LoadResult.Page(
                data = notificationsResponse.notifications,
                prevKey = if (currentOffset == INITIAL_OFFSET) null else currentOffset - PAGE_LIMIT_AND_OFFSET,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationsResponse.Notification>): Int {
        return 0
    }

}