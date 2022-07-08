package com.mafqud.android.results.contact

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.results.contactSuccess.models.ContactRequest
import com.mafqud.android.results.states.success.SuccessResultsIntent
import com.mafqud.android.results.states.success.models.NationalIdBody
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.launch


class ContactRepository @Inject constructor() : BaseRepository() {

    suspend fun contactDone(caseID: Int): Result<Boolean> {
        return safeApiCall {
            remoteDataManager.updateCaseContact(
                caseID = caseID,
            )
            return@safeApiCall true
        }
    }

    suspend fun contactFailed(caseID: Int): Result<Boolean> {
        return safeApiCall {
            remoteDataManager.publishCase(
                caseID = caseID,
            )
            return@safeApiCall true
        }
    }

    suspend fun createContact(caseID: Int): Result<Int?> {
        return safeApiCall {
            return@safeApiCall remoteDataManager.createCaseContact(
                ContactRequest(
                    caseId = caseID
                )
            ).id
        }
    }
}