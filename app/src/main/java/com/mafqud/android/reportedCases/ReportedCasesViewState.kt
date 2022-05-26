package com.mafqud.android.reportedCases

import androidx.paging.PagingData
import com.mafqud.android.home.CasesType
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.report.uploading.CaseItem
import com.mafqud.android.reportedCases.models.ReportedCasesResponse
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow


data class ReportedCasesViewState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val networkError: Result.NetworkError? = null,
    val cases: Flow<PagingData<ReportedCasesResponse.UserCase>>? = null,
)