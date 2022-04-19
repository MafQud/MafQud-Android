package com.mafqud.android.util.network

import android.content.Context
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.mafqud.android.R
import com.mafqud.android.ui.other.snakeBarMessage
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.TextUi


@Composable
fun Result.NetworkError.ShowNetworkErrorUi(onTryBtnClicked: () -> Unit, view: View) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(view, getErrorType(this.type))
        Result.NetworkError.NoInternet -> NoInternet(onTryBtnClicked)
    }
}

@Composable
fun HandlePagingError(
    loadState: CombinedLoadStates, onNoInternet: @Composable () -> Unit = {},
    onEmptyView: @Composable () -> Unit = {}
) {
    when {
        loadState.refresh is LoadState.Loading -> {
            CircleLoading(loadingState = true, circleSize = 20.dp)
        }
        loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached -> {
            onEmptyView()
        }
        loadState.append is LoadState.Loading -> {
            CircleLoading(loadingState = true, circleSize = 20.dp)
        }
        loadState.refresh is LoadState.Error -> {
            // val e = loadState.append as LoadState.Error
            onNoInternet()
        }
        loadState.append is LoadState.Error -> {
            val e = loadState.append as LoadState.Error
            val v = loadState.append.endOfPaginationReached
            Log.i("HandlePagingError: ", v.toString())
            val error = getNetworkErrorFromThrowable(e.error)
            val messageError = when (error) {
                is Result.NetworkError.Generic -> getErrorType(error.type)
                Result.NetworkError.NoInternet -> stringResource(id = R.string.error_no_more_data)
            }
            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = messageError,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun Result.NetworkError.ShowNetworkErrorSnakeBarUi(view: View) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(view, getErrorType(this.type))
        Result.NetworkError.NoInternet -> snakeBarMessage(
            view,
            stringResource(id = R.string.error_no_internet)
        )
    }
}

fun Result.NetworkError.ShowNetworkErrorSnakeBarUi(view: View, context: Context) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(view, context.getErrorType(this.type))
        Result.NetworkError.NoInternet -> snakeBarMessage(
            view,
            context.getString(R.string.error_no_internet)
        )
    }
}