package com.mafqud.android.auth.register

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) :
    BaseViewModel<RegisterIntent, RegisterViewState>(RegisterViewState()) {

    init {
        handleIntents {
            when (it) {
                RegisterIntent.DisplayGovAndCity -> getGovAndCity()
                is RegisterIntent.Signup -> signUp(it)
                is RegisterIntent.ValidateEmail -> validateEmail(it)
                is RegisterIntent.ValidatePhone -> validatePhone(it)
            }
        }
    }

    private fun getGovAndCity() {

    }

    private fun validateEmail(registerIntent: RegisterIntent.ValidateEmail) {
        emitLoadingState()
        launchViewModelScope {
            val result = registerRepository.isValidEmail(registerIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitEmailState(result.data)
            }
        }
    }

    private fun emitEmailState(data: String) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isValidEmail = true,
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = null,
                    data = data
                )
            )
        }
    }

    private fun validatePhone(registerIntent: RegisterIntent.ValidatePhone) {
        emitLoadingState()
        launchViewModelScope {
            val result = registerRepository.isValidPhone(registerIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitPhoneState(result.data)
            }
        }
    }

    private fun emitPhoneState(data: String) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isValidPhone = true,
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = null,
                    data = data
                )
            )
        }

    }


    private fun signUp(registerIntent: RegisterIntent.Signup) {
        emitLoadingState()
        launchViewModelScope {
            val result = registerRepository.signUp(registerIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitSignUpState(result.data)
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

    private fun emitSignUpState(data: String) {
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
        registerRepository.saveUser()
    }


    private fun emitLoadingState(
        isValidPhone: Boolean = false,
        isValidEmail: Boolean = false
    ) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isValidPhone = isValidPhone,
                    isValidEmail = isValidEmail,
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