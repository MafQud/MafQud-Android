package com.mafqud.android.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager

const val INITIAL_PAGE = 1

class NotificationSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, NotificationResponse.Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationResponse.Data> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE
            val restaurantsResponse = remoteData.getNotifications(
                page = currentPage,
            )


            val nextPage: Int? = if (restaurantsResponse.data.isEmpty()) null else currentPage + 1


            LoadResult.Page(
                data = restaurantsResponse.data,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationResponse.Data>): Int {
        return 0
    }

}