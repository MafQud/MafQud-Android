package com.mafqud.android.report.lost

import android.net.Uri
import com.mafqud.android.auth.login.LoginRepository
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.locations.LocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportLostRepository: ReportLostRepository,
    private val locationUseCase: LocationUseCase
) :
    BaseViewModel<ReportIntent, ReportViewState>(ReportViewState()) {

    init {
        handleIntents {
            when (it) {
                is ReportIntent.SaveImagesUri -> saveImages(it)
                is ReportIntent.RemoveImageUri -> removeImage(it)
                is ReportIntent.GetCities -> getCities(it)
                ReportIntent.GetGovs -> getGovs()
            }
        }
    }

    private fun getGovs() {
       launchViewModelScope {
           val result = locationUseCase.getGovs()
           _stateChannel.tryEmit(
               stateChannel.value.copy(
                   govs = result
               )
           )
       }
    }

    private fun getCities(it: ReportIntent.GetCities) {
        launchViewModelScope {
            // first clear the old cities
            _stateChannel.tryEmit(
                stateChannel.value.copy(
                    cities = null
                )
            )
            // get new
            val result = locationUseCase.getGCities(it.cityId)
            _stateChannel.tryEmit(
                stateChannel.value.copy(
                    cities = result
                )
            )
        }
    }

    private fun removeImage(it: ReportIntent.RemoveImageUri) {
        val currentImages: MutableList<Uri> = _stateChannel.value.imagesUri.toMutableList()
        currentImages.remove(it.imagesUri)
        _stateChannel.tryEmit(
            _stateChannel.value.copy(
                imagesUri = currentImages
            )
        )
    }

    private fun saveImages(it: ReportIntent.SaveImagesUri) {
        val currentImages = _stateChannel.value.imagesUri.toMutableList()
        currentImages.addAll(it.imagesUri.toList())
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                imagesUri = currentImages
            )
        )
    }

}