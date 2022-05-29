package com.mafqud.android.results.cases

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject


@HiltViewModel
class ResultCasesViewModel @Inject constructor(private val resultCaseRepository: ResultCaseRepository) :
    BaseViewModel<ResultCaseIntent, ResultCaseViewState>(ResultCaseViewState(isLoading = true)) {

    init {
        handleIntents {
            when (it) {
                is ResultCaseIntent.GetMatchesCases -> getAllData(it.caseId, isRefreshing = false)
                is  ResultCaseIntent.Refresh -> refreshData(it.caseId)
            }
        }
    }

    private fun getCase(case: ResultCaseIntent.GetMatchesCases) {
        getAllData(case.caseId,isRefreshing = false)
    }

    private fun refreshData(caseId: Int) {
        emitRefreshingState()
        getAllData(caseId, true)
    }

    private fun emitRefreshingState() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                networkError = null,
                isRefreshing = true,
            )
        )

    }

    private fun getAllData(caseID: Int, isRefreshing: Boolean) {
        launchViewModelScope {
            if (!isRefreshing) {
                emitLoadingState()
            }
            getCases(caseID)
        }
    }

    private suspend fun getCases(caseID: Int) {
        val result = resultCaseRepository.getMatchesCases(caseID)
        when (result) {
            is Result.Success -> emitCasesData(result.data)
            is Result.NetworkError.Generic -> emitGenericFailedState(result)
            Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
        }
    }

    private fun emitCasesData(data: Pager<Int, CasesMatchesResponse.CaseMatch>) {
        val cases = data.flow.distinctUntilChangedBy {
        }.cachedIn(viewModelScope)

        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    networkError = null,
                    errorMessage = null,
                    isRefreshing = false,
                    cases = cases
                )
            )
        }
    }


    private fun emitLoadingState() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = true,
                errorMessage = null,
                networkError = null,
                isRefreshing = false,
            )
        )

    }

    private fun emitGenericFailedState(error: Result.NetworkError.Generic) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorMessage = "May be",
                networkError = error,
                isRefreshing = false,
            )
        )

    }

    private fun emitInternetFailedState(result: Result.NetworkError.NoInternet) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    networkError = result,
                    isRefreshing = false,
                )
            )
        }
    }
}