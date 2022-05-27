package com.mafqud.android.results.publishCase

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.results.states.success.SuccessResultsIntent
import com.mafqud.android.results.states.success.SuccessResultsRepository
import com.mafqud.android.results.states.success.SuccessResultsViewState
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PublishCaseViewModel @Inject constructor(private val publishCaseRepository: PublishCaseRepository) :
    BaseViewModel<PublishCaseIntent, PublishCaseViewState>(PublishCaseViewState()) {

    init {
        handleIntents {
            when (it) {
                is PublishCaseIntent.Publish -> publishCase(it)
            }
        }
    }

    private fun publishCase(it: PublishCaseIntent.Publish) {
        emitLoadingState()
        launchViewModelScope {
            val result = publishCaseRepository.publishCase(it.caseID)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitSuccessStat()
            }
        }
    }

    private fun emitInternetFailedState(result: Result.NetworkError.NoInternet) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorFieldMessage = result.toString(),
                networkError = result,
                isSuccess = false,
            )
        )
    }

    private fun emitGenericFailedState(error: Result.NetworkError.Generic) {
        LogMe.i("genericError", error.error.message.toString())
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                errorFieldMessage = error.error.message,
                isLoading = false,
                networkError = error,
                isSuccess = false,
            )
        )
    }

    private fun emitSuccessStat() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorFieldMessage = null,
                networkError = null,
                isSuccess = true,
            )
        )
    }


    private fun emitLoadingState() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = true,
                    errorFieldMessage = null,
                    networkError = null,
                    isSuccess = false,
                )
            )
        }
    }


}