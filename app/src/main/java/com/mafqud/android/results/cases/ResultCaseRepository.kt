package com.mafqud.android.results.cases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.home.CasesSource
import com.mafqud.android.home.CasesType
import com.mafqud.android.home.PAGE_SIZE_PAGING_EXPLORE
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.results.states.success.SuccessResultsIntent
import com.mafqud.android.results.states.success.models.NationalIdBody
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.launch


class ResultCaseRepository @Inject constructor() : BaseRepository() {


    suspend fun getMatchesCases(caseID: Int): Result<Pager<Int, CasesMatchesResponse.CaseMatch>> {
        return safeApiCall {
            return@safeApiCall Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_EXPLORE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                MatchesCasesSource(remoteDataManager, caseID)
            })
        }
    }


}