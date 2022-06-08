package com.mafqud.android.map

import android.location.Location
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.home.CasesSource
import com.mafqud.android.home.CasesTabType
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import javax.inject.Inject

const val PAGE_SIZE_PAGING_EXPLORE = 10

class MapRepository @Inject constructor() : BaseRepository() {

    suspend fun getCases(
        casesTabType: CasesTabType,
        location: Location?
    ): Result<CasesDataResponse> {
        // TODO pass location here
        return safeApiCall {
            val type = when (casesTabType) {
                CasesTabType.ALL -> ""
                CasesTabType.MISSING -> "M"
                CasesTabType.FOUND -> "F"
            }
            return@safeApiCall remoteDataManager.getCases(
                offset = 0,
                limit = 10,
                type = type
            )
        }
    }

    suspend fun getCurrentUserName(): String {
        return getDisplayUserName()
    }

    suspend fun saveLocation(location: Location) {
        saveUserLocation(location)
    }

    suspend fun getSavedLocation(): Location? {
        return getUserLocation()
    }

}