package com.mafqud.android.reportedCases

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.reportedCases.models.ReportedCasesResponse
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject


@HiltViewModel
class ReportedCasesLimitedViewModel @Inject constructor(private val reportedCasesRepository: ReportedCasesRepository) :
    BaseViewModel<ReportedCasesIntent, ReportedCasesViewState>(ReportedCasesViewState(isLoading = true)) {

    init {
        handleIntents {
            when (it) {
                ReportedCasesIntent.GetReportedCasesLimited -> getCase()
                ReportedCasesIntent.Refresh -> refreshData()
                is ReportedCasesIntent.ArchiveCase -> archiveCase(it)
                is ReportedCasesIntent.FinishCase -> finishCase(it)
                //ReportedCasesIntent.GetReportedCases -> TODO()
            }
        }
    }

    private fun finishCase(it: ReportedCasesIntent.FinishCase) {
        emitLoadingState()
        launchViewModelScope {
            finishUserCase(it.caseId)
        }

    }

    private suspend fun finishUserCase(caseId: Int) {
        val result = reportedCasesRepository.finishCase(caseId)
        when (result) {
            is Result.Success -> emitFinishCase()
        }
    }

    private fun emitFinishCase() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                networkError = null,
                isRefreshing = true,
                isFinished = true
            )
        )
        refreshData()
    }

    private fun archiveCase(it: ReportedCasesIntent.ArchiveCase) {
        emitLoadingState()
        launchViewModelScope {
            archiveUserCase(it.caseId)
        }
    }

    private suspend fun archiveUserCase(caseId: Int) {
        val result = reportedCasesRepository.archiveCase(caseId)
        when (result) {
            is Result.Success -> emitArchiveCase()
        }
    }

    private fun emitArchiveCase() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                networkError = null,
                isRefreshing = true,
                isArchived = true
            )
        )
        refreshData()
    }

    private fun getCase() {
        getAllData(isRefreshing = false)
    }

    private fun refreshData() {
        emitRefreshingState()
        getAllData(true)
    }

    private fun emitRefreshingState() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                networkError = null,
                isRefreshing = true,
                isFinished = null,
                isArchived = null
            )
        )

    }

    private fun getAllData(isRefreshing: Boolean) {
        launchViewModelScope {
            if (!isRefreshing) {
                emitLoadingState()
            }
            getCases()
        }
    }

    private suspend fun getCases() {
        val result = reportedCasesRepository.getReportedCasesLimited()
        when (result) {
            is Result.Success -> emitCasesData(result.data)
        }
    }

    private fun emitCasesData(data: Pager<Int, ReportedCasesResponse.UserCase>) {
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
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorMessage = null,
                networkError = result,
                isRefreshing = false,
            )
        )
    }
}