package com.mafqud.android.notification

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepository: NotificationRepository) :
    BaseViewModel<NotificationIntent, NotificationViewState>(NotificationViewState(isLoading = true)) {

    init {
        handleIntents {
            when (it) {
                NotificationIntent.GetAllData -> getAllData(false)
                NotificationIntent.RefreshData -> refreshData()
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
                errorMessage = null,
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
            getNotifications()
        }
    }

    private suspend fun getNotifications() {
        val result = notificationRepository.getNotifications()
        when (result) {
            is Result.Success -> emitNotificationsData(result.data)
            is Result.NetworkError.Generic -> emitGenericFailedState(result)
            Result.NetworkError.NoInternet -> emitInternetFailedState(result as Result.NetworkError.NoInternet)
        }
    }

    private fun emitNotificationsData(data: Pager<Int, NotificationResponse.Data>) {
        val notifications = data.flow.distinctUntilChangedBy {
        }.cachedIn(viewModelScope)

        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    networkError = null,
                    isRefreshing = false,
                    notifications = notifications
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
        launchViewModelScope {
            _stateChannel.emit(
                stateChannel.value.copy(
                    isLoading = false,
                    errorMessage = "May be",
                    networkError = error,
                    isRefreshing = false,
                )
            )
        }
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