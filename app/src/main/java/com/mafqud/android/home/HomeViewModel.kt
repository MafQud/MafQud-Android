package com.mafqud.android.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.locations.LocationUseCase
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val locationUseCase: LocationUseCase
) :
    BaseViewModel<HomeIntent, HomeViewState>(HomeViewState(isLoading = true)) {

    init {
        handleIntents {
            when (it) {
                is HomeIntent.GetCases -> getCase(it.casesTabType)
                HomeIntent.Refresh -> refreshData()
                is HomeIntent.GetCasesByAge -> getCaseByAge(it)
                is HomeIntent.GetCasesByName -> getCaseByName(it)
                is HomeIntent.GetCasesByGov -> getCaseByGov(it)
                is HomeIntent.GetCasesByNoName -> getCaseByNoName()
                is HomeIntent.GetCasesByDate -> getCaseByDate(it)
            }
        }
    }

    private fun getCaseByDate(it: HomeIntent.GetCasesByDate) {
        setDateRange(it.dateRange)
        getAllData(isRefreshing = false)
    }

    private fun setDateRange(dateRange: DateRange) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                dateRange = dateRange
            )
        )
    }

    private fun getCaseByNoName() {
        setNoName()
        getAllData(isRefreshing = false)
    }

    private fun setNoName() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isNoName = !stateChannel.value.isNoName
            )
        )
    }

    private fun getCaseByGov(it: HomeIntent.GetCasesByGov) {
        setGovId(it.id)
        getAllData(isRefreshing = false)
    }

    private fun setGovId(id: Int?) {
        LogMe.i("setGovId", id.toString())
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                govID = id
            )
        )
    }


    private fun getGovs() {
        if (_stateChannel.value.govs == null) {
            launchViewModelScope {
                val result = locationUseCase.getGovs()
                _stateChannel.tryEmit(
                    stateChannel.value.copy(
                        govs = result
                    )
                )
            }
        }
    }

    private fun getCaseByName(it: HomeIntent.GetCasesByName) {
        setSearchedName(it.name)
        getAllData(isRefreshing = false)
    }

    private fun setSearchedName(name: String) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                searchedName = name
            )
        )
    }

    private fun getCaseByAge(it: HomeIntent.GetCasesByAge) {
        setAgeRange(it.ageRange)
        getAllData(isRefreshing = false)
    }

    private fun setAgeRange(ageRange: AgeRange) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                ageRange = ageRange
            )
        )
    }

    private fun getCase(casesTabType: CasesTabType) {
        getGovs()
        setUserName()
        setSelectedCasesType(casesTabType)
        getAllData(isRefreshing = false)
    }

    private fun setSelectedCasesType(casesTabType: CasesTabType) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                casesTabType = casesTabType
            )
        )
    }

    private fun getSelectedCasesType(): CasesTabType {
        return stateChannel.value.casesTabType
    }

    private fun setUserName() {
        if (stateChannel.value.userName == null) {
            launchViewModelScope {
                _stateChannel.tryEmit(
                    stateChannel.value.copy(
                        userName = homeRepository.getCurrentUserName()
                    )
                )
            }
        }
    }

    private fun refreshData() {
        resetNoName()
        restAgeRange()
        restDateRange()
        resetSearchName()
        resetGov()
        emitRefreshingState()
        getAllData(true)
    }

    private fun restDateRange() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                dateRange = null
            )
        )
    }

    private fun resetNoName() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isNoName = false
            )
        )
    }

    private fun resetGov() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                govID = null
            )
        )
    }

    private fun resetSearchName() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                searchedName = null
            )
        )
    }

    private fun restAgeRange() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                ageRange = null
            )
        )
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
        val result = homeRepository.getCases(
            getSelectedCasesType(),
            getAgeRange(),
            geSearchName(),
            getGovID(),
            getIsNoName(),
            getDateRange()
        )
        when (result) {
            is Result.Success -> emitNotificationsData(result.data)
        }
    }

    private fun getDateRange() = _stateChannel.value.dateRange

    private fun getIsNoName() = _stateChannel.value.isNoName

    private fun getGovID() = _stateChannel.value.govID

    private fun geSearchName() = _stateChannel.value.searchedName

    private fun getAgeRange() = _stateChannel.value.ageRange


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