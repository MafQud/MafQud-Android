package com.mafqud.android.report.uploading

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UploadingViewModel @Inject constructor(private val uploadingRepository: UploadingRepository) :
    BaseViewModel<UploadingIntent, UploadingViewState>(UploadingViewState()) {

    init {
        handleIntents {
            when (it) {
                UploadingIntent.UploadCaseOnly -> uploadCase(it)
                is UploadingIntent.UploadCaseWithImages -> uploadImagesThenCases(it)
                UploadingIntent.UploadImagesOnly -> uploadImages(it)
            }
        }
    }

    private fun uploadImages(it: UploadingIntent) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(isUploadingImages = true, isFailedUploadingImages = false)
        )
        uploadImagesThenCase()
    }

    private fun uploadCase(it: UploadingIntent) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(isUploadingCase = true, isFailedUploadingCase = false)
        )
        launchViewModelScope {
            uploadingCase(true)
        }
    }

    private fun uploadImagesThenCases(it: UploadingIntent.UploadCaseWithImages) {
        // first save images uris and case item to view state
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                imagesUrisPicked = it.imagesUrisPicked,
                caseData = it.caseItem,
                isUploadingImages = true
            )
        )
        uploadImagesThenCase()
    }

    private fun uploadImagesThenCase() {
        // then try uploading images and get uploaded images urls
        launchViewModelScope {
            val imagesResult =
                uploadingRepository.uploadImages(stateChannel.value.imagesUrisPicked)

            when (imagesResult) {
                is Result.NetworkError.Generic,
                Result.NetworkError.NoInternet -> {
                    emitFailedUploadingImages()
                }
                is Result.Success -> {
                    // first emit uploading images success state
                    emitSuccessUploadingImages(imagesResult.data)
                    //then try uploading case with images urls
                    uploadingCase(false)

                }
            }
        }
    }

    private suspend fun uploadingCase(success: Boolean = false) {
        // then try uploading case item
        val caseItem = stateChannel.value.caseData
        val caseResult =
            uploadingRepository.uploadCase(caseItem = caseItem)

        when (caseResult) {
            is Result.NetworkError.Generic, Result.NetworkError.NoInternet -> {
                emitFailedUploadingCase()
            }
            is Result.Success -> {
                // emit uploading images success state
                emitSuccessUploadingCase()

            }
        }
    }

    private fun emitSuccessUploadingCase() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isUploadingImages = false,
                    isFailedUploadingImages = false,
                    isSuccessUploadingImages = true,
                    isSuccess = true,
                    isUploadingCase = false,
                    isSuccessUploadingCase = true,
                    isFailedUploadingCase = false
                )
            )
        }
    }

    private fun emitFailedUploadingCase() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isUploadingImages = false,
                    isFailedUploadingImages = false,
                    isSuccessUploadingImages = true,
                    isSuccess = null,
                    isUploadingCase = false,
                    isSuccessUploadingCase = false,
                    isFailedUploadingCase = true
                )
            )
        }
    }

    private suspend fun emitSuccessUploadingImages(imagesUrlsUploaded: Boolean) {
        _stateChannel.emit(
            stateChannel.value.copy(
                isUploadingImages = false,
                isFailedUploadingImages = false,
                isSuccessUploadingImages = true,
                isSuccess = null,
                //imagesUrlsUploaded = imagesUrlsUploaded,
                isUploadingCase = true
            )
        )

    }

    private fun emitFailedUploadingImages() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isUploadingImages = false,
                    isFailedUploadingImages = true,
                    isSuccessUploadingImages = false,
                    isSuccess = null,
                )
            )
        }
    }

}