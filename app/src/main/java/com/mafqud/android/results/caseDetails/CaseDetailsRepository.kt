package com.mafqud.android.results.caseDetails

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.home.CasesSource
import com.mafqud.android.home.CasesType
import com.mafqud.android.home.PAGE_SIZE_PAGING_EXPLORE
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.results.caseDetails.models.CaseDetailsResponse
import com.mafqud.android.results.cases.MatchesCasesSource
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.results.states.success.SuccessResultsIntent
import com.mafqud.android.results.states.success.models.NationalIdBody
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.launch


class CaseDetailsRepository @Inject constructor() : BaseRepository() {


    suspend fun getCase(caseID: Int): Result<CaseDetailsResponse> {
        return safeApiCall {
            LogMe.i("getCase Result" , caseID.toString())
            return@safeApiCall remoteDataManager.getCaseDetails(caseID)
        }
    }


}