package com.mafqud.android.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import kotlinx.coroutines.delay

const val INITIAL_PAGE = 1

class NotificationSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, NotificationResponse.Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationResponse.Data> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE
            /*val notifications = remoteData.getNotifications(
                page = currentPage,
            )


            val nextPage: Int? = if (notifications.data.isEmpty()) null else currentPage + 1

*/
            val noty = listOf(
                NotificationResponse.Data(),
                NotificationResponse.Data(),
                NotificationResponse.Data(),
            )
            delay(1000)
            //throw Exception()
            LoadResult.Page(
                //TODO
                //data = notifications.data,
                data = emptyList(),
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage - 1,
                //TODO
                //nextKey = nextPage
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationResponse.Data>): Int {
        return 0
    }

}