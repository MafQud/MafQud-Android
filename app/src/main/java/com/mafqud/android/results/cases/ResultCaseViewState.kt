package com.mafqud.android.results.cases

import androidx.paging.PagingData
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.report.uploading.CaseItem
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow


data class ResultCaseViewState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val networkError: Result.NetworkError? = null,
    val cases: Flow<PagingData<CasesMatchesResponse.CaseMatch>>? = null,
)