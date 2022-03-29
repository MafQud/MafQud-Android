package com.mafqud.android.notification

import androidx.paging.PagingData
import com.mafqud.android.util.network.Result
import kotlinx.coroutines.flow.Flow

data class NotificationViewState(
    /**
     *  ui data
     */
    val notifications: Flow<PagingData<NotificationResponse.Data>>? = null,
    /**
     * ui state
     */
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val networkError: Result.NetworkError? = null,
)