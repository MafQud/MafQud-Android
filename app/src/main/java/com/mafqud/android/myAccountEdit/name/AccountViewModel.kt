package com.mafqud.android.myAccountEdit.name

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.other.LogMe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    BaseViewModel<AccountIntent, AccountViewState>(AccountViewState()) {

    init {
        handleIntents {
            when (it) {
                is AccountIntent.ChangeName -> changeName(it)
                AccountIntent.CurrentName -> getCurrentName()
            }
        }
    }

    private fun getCurrentName() {
        launchViewModelScope {
            val name = accountRepository.getCurrentName()
            emitCurrentName(name)
        }
    }

    private fun emitCurrentName(name: String) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                userName = name
            )
        )
    }

    private fun changeName(it: AccountIntent.ChangeName) {
        emitLoadingState()
        launchViewModelScope {
            val result = accountRepository.changeUserName(it.userName)
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