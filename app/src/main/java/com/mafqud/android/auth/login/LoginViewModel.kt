package com.mafqud.android.auth.login

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    BaseViewModel<LoginIntent, LoginViewState>(LoginViewState()) {

    init {
        handleIntents {
            when (it) {
                is LoginIntent.PhoneLogin -> loginWithPhone(it)
            }
        }
    }


    private fun loginWithPhone(registerIntent: LoginIntent.PhoneLogin) {
        emitLoadingState()
        launchViewModelScope {
            val result = loginRepository.loginWithNumber(registerIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitSuccessState(result.data)
            }
        }
    }

    private fun emitInternetFailedState(result: Result.NetworkError.NoInternet) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = result,
                    isSuccess = false,
                    data = null
                )
            )
        }
    }

    private fun emitGenericFailedState(error: Result.NetworkError.Generic) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorFieldMessage = "",
                    networkError = error,
                    isSuccess = false,
                    data = null
                )
            )
        }
    }

    private fun emitSuccessState(data: String) {
        launchViewModelScope {
            saveUserData()
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = null,
                    isSuccess = true,
                    data = data
                )
            )
        }
    }

    private suspend fun saveUserData(/*user: AuthResponseSuccess.User*/) {
        loginRepository.saveUser()
    }


    private fun emitLoadingState() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = true,
                    errorFieldMessage = null,
                    networkError = null,
                    isSuccess = false,
                    data = null
                )
            )
        }
    }

}