package com.mafqud.android.util.network

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.mafqud.android.R
import com.mafqud.android.ui.other.snakeBarMessage
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.ImageUi
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
    loadState: CombinedLoadStates,
    modifier: Modifier,
    onRetry: () -> Unit,
) {
    when {
        loadState.refresh is LoadState.Loading -> {
            BoxUi(modifier = modifier) {
                CircleLoading(loadingState = true, circleSize = 35.dp)
            }
        }
        loadState.append is LoadState.Loading -> {
            CircleLoading(loadingState = true, circleSize = 20.dp)
        }
        loadState.refresh is LoadState.Error -> {
            val e = loadState.refresh as LoadState.Error
            BoxUi(modifier, contentAlignment = Alignment.Center) {
                BoxUi(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                    ErrorItem(
                        message = e.error.getErrorMessage(),
                        onClickRetry = { onRetry() }
                    )
                }
            }
        }
        loadState.append is LoadState.Error -> {
            val e = loadState.append as LoadState.Error
            BoxUi(modifier) {
                ErrorEndItem(
                    message = e.error.getErrorMessage(),
                    onClickRetry = { onRetry() }
                )
            }
        }
    }
}

@Composable
private fun Throwable.getErrorMessage(): String {
    val networkError  = getNetworkErrorFromThrowable(throwable = this)
    return when(networkError) {
        is Result.NetworkError.Generic -> getMessageFromGeneric(networkError)
        Result.NetworkError.NoInternet -> stringResource(id = R.string.error_no_internet)
    }
}

@Composable
fun getMessageFromGeneric(networkError: Result.NetworkError.Generic) : String {
    return when(networkError.type) {
        HttpErrorType.BadGateway -> stringResource(id = R.string.error_bad_gateway)
        HttpErrorType.BadRequest -> stringResource(id = R.string.error_bad_request)
        is HttpErrorType.DataInvalid -> stringResource(id = R.string.error_unknown)
        HttpErrorType.Forbidden -> stringResource(id = R.string.error_forbidden)
        HttpErrorType.InternalServerError -> stringResource(id = R.string.error_server_error)
        HttpErrorType.NotAuthorized -> stringResource(id = R.string.error_not_auth)
        HttpErrorType.NotFound -> stringResource(id = R.string.error_not_found)
        HttpErrorType.Unknown -> stringResource(id = R.string.error_unknown)
    }
}

@Composable
fun ErrorEndItem(message: String, onClickRetry: () -> Unit) {
    ColumnUi(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextUi(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )
        // next
        TextUi(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onClickRetry()
                }
                .padding(8.dp),
            text = stringResource(id = R.string.try_again),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorItem(
    message: String,
    onClickRetry: () -> Unit = {}
) {
    ColumnUi(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageUi(
            painter = painterResource(id = R.drawable.ic_failure_robot),
            modifier = Modifier.size(287.dp, 188.dp)
        )
        TextUi(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )
        // next
        TextUi(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onClickRetry()
                }
                .padding(8.dp),
            text = stringResource(id = R.string.try_again),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
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