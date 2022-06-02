package com.mafqud.android.results.caseDetails

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.results.caseDetails.models.CaseDetailsResponse
import com.mafqud.android.results.cases.ResultCaseIntent
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CaseDetailsViewModel @Inject constructor(private val caseDetailsRepository: CaseDetailsRepository) :
    BaseViewModel<CaseDetailsIntent, CaseDetailsViewState>(CaseDetailsViewState(isLoading = true)) {


    init {
        handleIntents {
            when (it) {
                is CaseDetailsIntent.GetCase -> getAllData(it.caseId, isRefreshing = false)
                is  CaseDetailsIntent.Refresh -> refreshData(it.caseId)
            }
        }
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
                case = null
            )
        )

    }

    private fun getAllData(caseID: Int, isRefreshing: Boolean) {
        launchViewModelScope {
            if (!isRefreshing) {
                emitLoadingState()
            }
            getCase(caseID)
        }
    }

    private suspend fun getCase(caseID: Int) {
        LogMe.i("getCase" , caseID.toString())
        val result = caseDetailsRepository.getCase(caseID)
        when (result) {
            is Result.Success -> emitCasesData(result.data)
            is Result.NetworkError.Generic -> emitGenericFailedState(result)
            Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
        }
    }

    private fun emitCasesData(data: CaseDetailsResponse) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    networkError = null,
                    errorMessage = null,
                    isRefreshing = false,
                    case = data
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