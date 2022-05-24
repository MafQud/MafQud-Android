package com.mafqud.android.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.home.model.CasesDataResponse

const val INITIAL_PAGE = 1
const val PAGE_SIZE_PAGING = 10

class CasesSource(
    private val remoteData: RemoteDataManager,
    private val casesType: CasesType
) : PagingSource<Int, CasesDataResponse.Case>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, CasesDataResponse.Case> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE
            val type = when(casesType) {
                CasesType.ALL -> ""
                CasesType.MISSING -> "M"
                CasesType.FOUND -> "F"
            }
            val casesResponse = remoteData.getCases(
                page = currentPage,
                limit = PAGE_SIZE_PAGING,
                type = type
            )

            val nextPage: Int? = if (casesResponse.cases.isEmpty()) null else currentPage + 1


            PagingSource.LoadResult.Page(
                data = casesResponse.cases,
                prevKey = if (currentPage == INITIAL_PAGE) null else currentPage - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CasesDataResponse.Case>): Int {
        return 0
    }

}