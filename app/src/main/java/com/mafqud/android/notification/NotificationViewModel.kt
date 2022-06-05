package com.mafqud.android.notification

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.mafqud.android.base.viewModel.BaseViewModel
import com.mafqud.android.notification.models.NotificationsResponse
import com.mafqud.android.util.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationUseCase: NotificationUseCase) :
    BaseViewModel<NotificationIntent, NotificationViewState>(NotificationViewState(isLoading = true)) {

    init {
        handleIntents {
            when (it) {
                NotificationIntent.GetAllData -> getAllData(false)
                NotificationIntent.RefreshData -> refreshData()
                is NotificationIntent.MarkNotificationAsRead -> markNotificationRead(it)
            }
        }
    }

    private fun markNotificationRead(it: NotificationIntent.MarkNotificationAsRead) {
        launchViewModelScope {
            notificationUseCase.markNotificationAsRead(it.id)
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
        val notifications = notificationUseCase.getNotifications()?.distinctUntilChangedBy {
        }?.cachedIn(viewModelScope)

        _stateChannel.tryEmit(
            stateChannel.value.copy(
                isLoading = false,
                errorMessage = null,
                networkError = null,
                isRefreshing = false,
                notifications = notifications
            )
        )
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

}