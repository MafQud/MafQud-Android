package com.mafqud.android.map

import android.location.Location
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.home.*
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) :
    BaseViewModel<MapIntent, MapViewState>(MapViewState()) {

    init {
        handleIntents {
            when (it) {
                is MapIntent.GetCases -> getCases(it.casesTabType)
                MapIntent.Refresh -> refreshData()
                is MapIntent.SetMapType -> setMapType(it)
                is MapIntent.SaveLocation -> saveLocation(it)
            }
        }
    }

    private fun saveLocation(it: MapIntent.SaveLocation) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                location = it.location
            )
        )
        LogMe.i("location saved", it.location.longitude.toString())
        launchViewModelScope {
            mapRepository.saveLocation(it.location)
            getAllData(isRefreshing = true)
        }
    }

    private fun setMapType(it: MapIntent.SetMapType) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                mapUiType = it.mapUiType
            )
        )
    }


    private fun getCases(casesTabType: CasesTabType) {
        saveCaseTape(casesTabType)
        launchViewModelScope {
            val savedLocation = mapRepository.getSavedLocation()
            setLocation(savedLocation)
            if (savedLocation == null) {
                _stateChannel.tryEmit(
                    stateChannel.value.copy(
                        mapUiType = MapUiType.REQUIRE_PERMISSIONS
                    )
                )
            } else {
                _stateChannel.tryEmit(
                    stateChannel.value.copy(
                        mapUiType = MapUiType.DISPLAY_CASES
                    )
                )
                getAllData(isRefreshing = false)
            }
        }

    }

    private fun setLocation(savedLocation: Location?) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                location = savedLocation
            )
        )
    }

    private fun saveCaseTape(casesTabType: CasesTabType) {
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
                        userName = mapRepository.getCurrentUserName()
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
        LogMe.e("getCases", "DONE")
        val result = mapRepository.getCases(
            getSelectedCasesType(), getSavedLocation()
        )
        when (result) {
            is Result.Success -> emitCasesData(result.data)
        }
    }

    private fun getSavedLocation(): Location? = _stateChannel.value.location

    private fun emitCasesData(data: CasesDataResponse) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    networkError = null,
                    errorMessage = null,
                    isRefreshing = false,
                    cases = data
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