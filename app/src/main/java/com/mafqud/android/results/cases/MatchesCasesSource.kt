package com.mafqud.android.results.cases

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.home.CasesType
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.util.other.LogMe

const val INITIAL_OFFSET = 0
const val PAGE_LIMIT_AND_OFFSET = 10

class MatchesCasesSource(
    private val remoteData: RemoteDataManager,
    private val caseId: Int
) : PagingSource<Int, CasesMatchesResponse.CaseMatch>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, CasesMatchesResponse.CaseMatch> {
        return try {
            val currentOffset = params.key ?: INITIAL_OFFSET
            LogMe.i("currentOffsetCases", currentOffset.toString())

            val casesResponse = remoteData.getMatchesCases(
                offset = currentOffset,
                limit = PAGE_LIMIT_AND_OFFSET,
                caseID = caseId
            )

            val nextPage: Int? = if (casesResponse.caseMatches.isEmpty()) null else currentOffset + PAGE_LIMIT_AND_OFFSET


            LoadResult.Page(
                data = casesResponse.caseMatches,
                prevKey = if (currentOffset == INITIAL_OFFSET) null else currentOffset - PAGE_LIMIT_AND_OFFSET,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CasesMatchesResponse.CaseMatch>): Int {
        return 0
    }

}