package com.mafqud.android.home

import android.util.Range
import androidx.annotation.Keep
import androidx.paging.PagingData
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.locations.MyGov
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow

@Keep
data class AgeRange(
    val start: Int,
    val end: Int,
)

@Keep
data class DateRange(
    //2021-04-29
    val start: String,
    //2022-08-15
    val end: String,
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
    val dateRange: DateRange? = null,
    val searchedName: String? = null,
    val isNoName: Boolean = false,
    val govs: List<MyGov>? = null,
    val govID: Int? = null,
)