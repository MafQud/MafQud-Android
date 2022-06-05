package com.mafqud.android.home

import androidx.paging.PagingData
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow


data class HomeViewState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val userName: String? = null,
    val networkError: Result.NetworkError? = null,
    val casesTabType: CasesTabType = CasesTabType.ALL,
    val cases: Flow<PagingData<CasesDataResponse.Case>>? = null,
)