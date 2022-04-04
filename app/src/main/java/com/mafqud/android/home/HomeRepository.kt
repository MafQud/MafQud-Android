package com.mafqud.android.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import javax.inject.Inject

const val PAGE_SIZE_PAGING_EXPLORE = 10

class HomeRepository @Inject constructor (private val remoteData: RemoteDataManager) {

    suspend fun getCases(): Result<Pager<Int, CasesDataResponse.Data>> {
        return safeApiCall {
            return@safeApiCall Pager(config = PagingConfig(
                pageSize = PAGE_SIZE_PAGING_EXPLORE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                CasesSource(remoteData)
            })
        }
    }

}