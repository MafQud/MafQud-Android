package com.mafqud.android.results.caseDetails

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.results.caseDetails.models.CaseDetailsResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.LogMe
import javax.inject.Inject


class CaseDetailsRepository @Inject constructor() : BaseRepository() {


    suspend fun getCase(caseID: Int): Result<CaseDetailsResponse> {
        return safeApiCall {
            LogMe.i("getCase Result" , caseID.toString())
            return@safeApiCall remoteDataManager.getCaseDetails(caseID)
        }
    }


}