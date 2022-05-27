package com.mafqud.android.results.publishCase

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.results.states.success.SuccessResultsIntent
import com.mafqud.android.results.states.success.models.NationalIdBody
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.launch


class PublishCaseRepository @Inject constructor() : BaseRepository() {

    suspend fun publishCase(caseID: Int): Result<Boolean> {
        return safeApiCall {
            remoteDataManager.publishCase(
                caseID = caseID,
            )
            return@safeApiCall true
        }
    }


}