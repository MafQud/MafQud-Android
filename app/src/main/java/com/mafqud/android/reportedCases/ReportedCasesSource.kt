package com.mafqud.android.reportedCases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.reportedCases.models.ReportedCasesResponse

const val INITIAL_OFFSET = 0
const val PAGE_LIMIT_AND_OFFSET = 10

class ReportedCasesSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, ReportedCasesResponse.UserCase>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, ReportedCasesResponse.UserCase> {
        return try {
            val currentPage = params.key ?: INITIAL_OFFSET
            val casesResponse = remoteData.getReportedCases(
                page = currentPage,
                limit = PAGE_LIMIT_AND_OFFSET,
            )

            val nextPage: Int? = if (casesResponse.userCases.isEmpty()) null else currentPage + PAGE_LIMIT_AND_OFFSET

            LoadResult.Page(
                data = casesResponse.userCases,
                prevKey = if (currentPage == INITIAL_OFFSET) null else currentPage - PAGE_LIMIT_AND_OFFSET,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReportedCasesResponse.UserCase>): Int {
        return 0
    }

}