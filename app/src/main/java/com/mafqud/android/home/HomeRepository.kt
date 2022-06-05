package com.mafqud.android.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import javax.inject.Inject

const val PAGE_SIZE_PAGING_EXPLORE = 10

class HomeRepository @Inject constructor () : BaseRepository() {

    suspend fun getCases(casesTabType: CasesTabType, ageRange: AgeRange?, searchName: String): Result<Pager<Int, CasesDataResponse.Case>> {
        return safeApiCall {
            return@safeApiCall Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_EXPLORE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                CasesSource(remoteDataManager, casesTabType, ageRange, searchName)
            })
        }
    }

    suspend fun getUserName(): String {
        return getDisplayUserName()
    }

}