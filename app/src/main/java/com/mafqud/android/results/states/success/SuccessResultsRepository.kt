package com.mafqud.android.results.states.success

import com.mafqud.android.base.BaseRepository
import com.mafqud.android.results.states.success.models.NationalIdBody
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.launch


class SuccessResultsRepository @Inject constructor() : BaseRepository() {

    suspend fun updateNationalID(addNationalId: SuccessResultsIntent.AddNationalId): Result<Boolean> {
        return safeApiCall {
            val userId = getUserID()
            remoteDataManager.setNationalId(
                userId = userId,
                NationalIdBody(
                    nationalId = addNationalId.nationalId,
                )
            )
            saveUserNationalID(addNationalId.nationalId)
            return@safeApiCall true
        }
    }


    private suspend fun saveUserNationalID(nationalId: String) {
        setNationalID(nationalId)
    }

}