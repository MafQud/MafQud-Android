package com.mafqud.android.auth.register

import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.locations.LocationUseCase
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val locationUseCase: LocationUseCase
    ) :
    BaseViewModel<RegisterIntent, RegisterViewState>(RegisterViewState()) {

    init {
        handleIntents {
            when (it) {
                //is RegisterIntent.ValidateEmail -> validateEmail(it)
                is RegisterIntent.ValidatePhone -> validatePhone(it)
                is RegisterIntent.VerifyOTP -> verifyOTP(it)
                is RegisterIntent.NextStep -> nextStep(it)
                is RegisterIntent.SaveName -> saveName(it)
                is RegisterIntent.GetCities -> getCities(it)
                is RegisterIntent.SaveLocation -> saveLocation(it)
                is RegisterIntent.SavePassword -> savePassAndSignup(it)
                RegisterIntent.Clear -> resetInitialState()
            }
        }
    }

    private fun resetInitialState() {
        _stateChannel.tryEmit(
            RegisterViewState()
        )
    }

    private fun savePassAndSignup(it: RegisterIntent.SavePassword) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                password = it.password,
            )
        )
        signUp()
    }

    private fun saveLocation(it: RegisterIntent.SaveLocation) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                stepCount = StepCount.Five,
                govId = it.govId,
                cityId = it.cityId,
            )
        )
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

    private fun getCities(it: RegisterIntent.GetCities) {
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

    private fun saveName(it: RegisterIntent.SaveName) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                name = it.name,
                stepCount = StepCount.Four
            )
        )
        getGovs()
    }

    private fun nextStep(it: RegisterIntent.NextStep) {
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                stepCount = it.stepCount
            )
        )
    }

    private fun verifyOTP(registerIntent: RegisterIntent.VerifyOTP) {
        emitLoadingState(isLoading = registerIntent.loading)
    }

    /*private fun validateEmail(registerIntent: RegisterIntent.ValidateEmail) {
        emitLoadingState()
        launchViewModelScope {
            val result = registerRepository.isValidEmail(registerIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitEmailState(result.data)
            }
        }
    }*/

    private fun emitEmailState(data: String) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = null,
                )
            )
        }
    }

    private fun validatePhone(registerIntent: RegisterIntent.ValidatePhone) {
        // save  entered phone
        _stateChannel.tryEmit(
            stateChannel.value.copy(
                phone = registerIntent.phone
            )
        )
        emitLoadingState()
        launchViewModelScope {
            val result = registerRepository.isValidPhone(registerIntent)
            when (result) {
                is Result.NetworkError.Generic -> emitNotValidPhoneState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitPhoneState()
            }
        }
    }

    private fun emitNotValidPhoneState(error: Result.NetworkError.Generic) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isValidPhone = false,
                    isLoading = false,
                    errorFieldMessage = "",
                    /*networkError = error,*/
                    isSuccess = false,
                )
            )
        }
    }

    private fun emitPhoneState() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isValidPhone = true,
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = null,
                    /*stepCount = StepCount.Two*/
                )
            )
        }

    }

    data class SignUpForm(
        val phone: String? = "",
        val fullName: String? = "",
        val govId: Int? = -1,
        val cityId: Int? = -1,
        val password: String? = "",
    )

    private fun signUp() {
        emitLoadingState()
        val name = _stateChannel.value.name
        val phone = _stateChannel.value.phone
        val govId = _stateChannel.value.govId
        val cityId = _stateChannel.value.cityId
        val password = _stateChannel.value.password

        launchViewModelScope {
            val result = registerRepository.signUp(SignUpForm(
                phone = phone,
                fullName = name,
                govId = govId,
                cityId = cityId,
                password = password,
            ))
            when (result) {
                is Result.NetworkError.Generic -> emitGenericFailedState(result.copy())
                Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
                is Result.Success -> emitSignUpState()
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
                    isValidPhone = false
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
                )
            )
        }
    }

    private fun emitSignUpState() {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorFieldMessage = null,
                    networkError = null,
                    isSuccess = true,
                )
            )
        }
    }


    private fun emitLoadingState(
        isValidPhone: Boolean? = null,
        isValidEmail: Boolean = false,
        isLoading: Boolean = true
    ) {
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isValidPhone = isValidPhone,
                    isLoading = isLoading,
                    errorFieldMessage = null,
                    networkError = null,
                    isSuccess = false,
                )
            )
        }
    }

}