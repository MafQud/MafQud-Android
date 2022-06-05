package com.mafqud.android.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.other.LogMe

const val INITIAL_OFFSET = 0
const val PAGE_LIMIT_AND_OFFSET = 10

class CasesSource(
    private val remoteData: RemoteDataManager,
    private val casesTabType: CasesTabType,
    private val ageRange: AgeRange?,
    private val searchName: String
) : PagingSource<Int, CasesDataResponse.Case>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, CasesDataResponse.Case> {
        return try {
            val currentOffset = params.key ?: INITIAL_OFFSET
            LogMe.i("currentOffsetCases", currentOffset.toString())
            val type = when (casesTabType) {
                CasesTabType.ALL -> ""
                CasesTabType.MISSING -> "M"
                CasesTabType.FOUND -> "F"
            }

            val casesResponse = if (ageRange != null) {
                remoteData.getCases(
                    offset = currentOffset,
                    limit = PAGE_LIMIT_AND_OFFSET,
                    type = type,
                    startAge = ageRange.start,
                    endAge = ageRange.end,
                    name = searchName,
                )
            } else {
                remoteData.getCases(
                    offset = currentOffset,
                    limit = PAGE_LIMIT_AND_OFFSET,
                    type = type,
                    name = searchName
                )
            }

            val nextPage: Int? =
                if (casesResponse.cases.isEmpty()) null else currentOffset + PAGE_LIMIT_AND_OFFSET


            LoadResult.Page(
                data = casesResponse.cases,
                prevKey = if (currentOffset == INITIAL_OFFSET) null else currentOffset - PAGE_LIMIT_AND_OFFSET,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CasesDataResponse.Case>): Int {
        return 0
    }

}