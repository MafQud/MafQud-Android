package com.mafqud.android.reportedCases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.reportedCases.models.ReportedCasesResponse

const val PAGE_LIMIT = 2

class ReportedCasesLimitedSource(
    private val remoteData: RemoteDataManager,
    private val limit: Int = PAGE_LIMIT
) : PagingSource<Int, ReportedCasesResponse.UserCase>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, ReportedCasesResponse.UserCase> {
        return try {
            val currentPage = params.key ?: INITIAL_OFFSET
            val casesResponse = remoteData.getReportedCases(
                page = currentPage,
                limit = limit,
            )

            LoadResult.Page(
                data = casesResponse.userCases,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReportedCasesResponse.UserCase>): Int {
        return 0
    }

}