package com.mafqud.android.reportedCases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.home.CasesType
import com.mafqud.android.home.model.CasesDataResponse
import java.io.IOException

const val INITIAL_PAGE = 1
const val PAGE_SIZE_PAGING = 10

class ReportedCasesSource(
    private val remoteData: RemoteDataManager,
) : PagingSource<Int, CasesDataResponse.Case>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, CasesDataResponse.Case> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE
            val casesResponse = remoteData.getReportedCases(
                page = currentPage,
                limit = PAGE_SIZE_PAGING,
            )
            //TODO delete -> IOException
            throw IOException()

            //TODO uncomment this
           /* val nextPage: Int? = if (casesResponse.isEmpty()) null else currentPage + 1

            LoadResult.Page(
                data = casesResponse.cases,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage - 1,
                nextKey = nextPage
            )*/
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CasesDataResponse.Case>): Int {
        return 0
    }

}