package com.mafqud.android.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    BaseViewModel<HomeIntent, HomeViewState>(HomeViewState(isLoading = true)) {

    init {
        handleIntents {
            when (it) {
                is HomeIntent.GetCases -> getCase(it.casesType)
                HomeIntent.Refresh -> refreshData()
            }
        }
    }

    private fun getCase(casesType: CasesType) {
        setUserName()
        setSelectedCasesType(casesType)
        getAllData(isRefreshing = false)
    }

    private fun setSelectedCasesType(casesType: CasesType) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                casesType = casesType
            )
        )
    }

    private fun getSelectedCasesType(): CasesType {
        return stateChannel.value.casesType
    }

    private fun setUserName() {
        if (stateChannel.value.userName == null) {
            launchViewModelScope {
                _stateChannel.tryEmit(
                    stateChannel.value.copy(
                        userName = homeRepository.getUserName()
                    )
                )
            }
        }
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
        val result = homeRepository.getCases(getSelectedCasesType())
        when (result) {
            is Result.Success -> emitNotificationsData(result.data)
        }
    }

    private fun emitNotificationsData(data: Pager<Int, CasesDataResponse.Case>) {
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