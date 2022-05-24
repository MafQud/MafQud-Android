package com.mafqud.android.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.notification.models.NotificationsResponse
import kotlinx.coroutines.delay

const val INITIAL_PAGE = 1

class NotificationSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, NotificationsResponse.Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationsResponse.Notification> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE
            val notificationsResponse = remoteData.getNotifications(
                page = currentPage,
            )

            val nextPage: Int? = if (notificationsResponse.notifications.isEmpty()) null else currentPage + 1
            LoadResult.Page(
                data = notificationsResponse.notifications,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage - 1,
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