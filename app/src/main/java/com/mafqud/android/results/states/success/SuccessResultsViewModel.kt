package com.mafqud.android.results.states.success

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SuccessResultsViewModel @Inject constructor(private val successResultsRepository: SuccessResultsRepository) :
    BaseViewModel<SuccessResultsIntent, SuccessResultsViewState>(SuccessResultsViewState()) {

    init {
        handleIntents {
            when (it) {
                is SuccessResultsIntent.AddNationalId -> setNationalId(it)
                SuccessResultsIntent.ClearState -> {
                    _stateChannel.tryEmit(
                        stateChannel.value.copy(
                            isSuccess = false,
                            errorFieldMessage = null,
                            networkError = null,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }

    private fun setNationalId(successResultsIntent: SuccessResultsIntent.AddNationalId) {
        emitLoadingState()
        launchViewModelScope {
            val result = successResultsRepository.updateNationalID(successResultsIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitSuccessState(successResultsIntent.nationalId)
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

    private fun emitSuccessState(nationalId: String) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorFieldMessage = null,
                networkError = null,
                isSuccess = true,
                nationalID = nationalId
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