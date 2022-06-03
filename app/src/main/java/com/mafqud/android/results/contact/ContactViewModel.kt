package com.mafqud.android.results.contact

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val contactRepository: ContactRepository) :
    BaseViewModel<ContactIntent, ContactViewState>(ContactViewState()) {

    init {
        handleIntents {
            when (it) {
                is ContactIntent.Done -> doneContact(it)
                is ContactIntent.Failed -> failedContact(it)
            }
        }
    }

    private fun doneContact(contactIntent: ContactIntent.Done) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = true,
                errorFieldMessage = null,
                networkError = null,
                isContactDoneSuccess = null,
                isContactFailedSuccess = null,
            )
        )
        launchViewModelScope {
            val result = contactRepository.contactDone(contactIntent.caseID)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitDoneSuccessStat()
            }
        }
    }

    private fun failedContact(contactIntent: ContactIntent.Failed) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = true,
                errorFieldMessage = null,
                networkError = null,
                isContactDoneSuccess = false,
                isContactFailedSuccess = false,
            )
        )
        launchViewModelScope {
            val result = contactRepository.contactFailed(contactIntent.caseID)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitFailedSuccessStat()
            }
        }
    }

    private fun emitInternetFailedState(result: Result.NetworkError.NoInternet) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorFieldMessage = result.toString(),
                networkError = result,
                isContactDoneSuccess = null,
                isContactFailedSuccess = null,
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
                isContactDoneSuccess = null,
                isContactFailedSuccess = null,
            )
        )
    }

    private fun emitDoneSuccessStat() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorFieldMessage = null,
                networkError = null,
                isContactDoneSuccess = true,
                isContactFailedSuccess = null,
            )
        )
    }


    private fun emitFailedSuccessStat() {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorFieldMessage = null,
                networkError = null,
                isContactDoneSuccess = null,
                isContactFailedSuccess = true,
            )
        )
    }
}