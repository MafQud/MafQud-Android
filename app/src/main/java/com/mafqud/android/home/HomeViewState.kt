package com.mafqud.android.home

import android.util.Range
import androidx.paging.PagingData
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.locations.MyGov
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow

data class AgeRange(
    val start: Int,
    val end: Int,
)

data class HomeViewState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val userName: String? = null,
    val networkError: Result.NetworkError? = null,
    val casesTabType: CasesTabType = CasesTabType.ALL,
    val cases: Flow<PagingData<CasesDataResponse.Case>>? = null,
    val ageRange: AgeRange? = null,
    val searchedName: String? = null,
    val govs: List<MyGov>? = null,
    val govID: Int? = null,
)