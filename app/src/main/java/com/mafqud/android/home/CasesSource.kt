package com.mafqud.android.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.home.model.CasesDataResponse

const val INITIAL_PAGE = 1

class CasesSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, CasesDataResponse.Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CasesDataResponse.Data> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE
            val cases = remoteData.getCases(
                page = currentPage,
            )


            val nextPage: Int? = if (cases.data.isEmpty()) null else currentPage + 1


            LoadResult.Page(
                data = cases.data,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CasesDataResponse.Data>): Int {
        return 0
    }

}