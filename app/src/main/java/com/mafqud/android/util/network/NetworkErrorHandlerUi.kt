package com.mafqud.android.util.network

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonSmall
import com.mafqud.android.ui.compose.ShouldLogoutDialog
import com.mafqud.android.ui.other.snakeBarMessage
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.ImageUi
import com.mafqud.android.ui.theme.TextUi
import org.greenrobot.eventbus.EventBus


@Composable
fun Result.NetworkError.ShowNetworkErrorUi(onTryBtnClicked: () -> Unit, view: View) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(
            view, getErrorType(
                this.type
            )
        )
        Result.NetworkError.NoInternet -> NoInternet(onTryBtnClicked)
    }
}

/**
 * @param isSimpleErrorItems
 * true -> for display only test with try button
 */
@Composable
fun HandlePagingError(
    loadState: CombinedLoadStates,
    modifier: Modifier,
    shouldShowLogoutDialog: Boolean = true,
    isSimpleErrorItems: Boolean = false,
    onRetry: () -> Unit,
) {
    val isDialogOpened = remember {
        mutableStateOf(false)
    }
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
                        isSimpleErrorItems = isSimpleErrorItems,
                        message = e.error.getErrorMessage(),
                        onClickRetry = { onRetry() }
                    )

                    val isUnauthorized = e.error.isUnauthorized()
                    if (isUnauthorized) {
                        isDialogOpened.value = true
                    }
                    if (shouldShowLogoutDialog) {
                        ShouldLogoutDialog(
                            titleHead = stringResource(id = R.string.should_logout),
                            isOpened = isDialogOpened, onConfirmClicked = {
                                EventBus.getDefault().post("logout")
                            }
                        )
                    }
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
    val networkError = getNetworkErrorFromThrowable(throwable = this)
    return when (networkError) {
        is Result.NetworkError.Generic -> getMessageFromGeneric(networkError)
        Result.NetworkError.NoInternet -> stringResource(id = R.string.error_no_internet)
    }
}

@Composable
private fun Throwable.isUnauthorized(): Boolean {
    return when (val res = getNetworkErrorFromThrowable(throwable = this)) {
        is Result.NetworkError.Generic -> res.type == HttpErrorType.NotAuthorized
        else -> false
    }
}

@Composable
fun getMessageFromGeneric(networkError: Result.NetworkError.Generic): String {
    return when (networkError.type) {
        HttpErrorType.BadGateway -> stringResource(id = R.string.error_bad_gateway)
        is HttpErrorType.BadRequest -> stringResource(id = R.string.error_bad_request)
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
@Preview
fun ErrorItem(
    message: String = "aaa",
    onClickRetry: () -> Unit = {},
    isSimpleErrorItems: Boolean = false
) {
    ColumnUi(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isSimpleErrorItems) {
            ImageUi(
                painter = painterResource(id = R.drawable.ic_no_internet),
                modifier = Modifier.size(243.dp, 236.dp)
            )
        }
        TextUi(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.titleMedium
        )
        // next
        ButtonSmall(title = stringResource(id = R.string.try_it)) {
            onClickRetry()
        }
        /*TextUi(
            modifier = Modifier
                .width(170.dp)
                .height(50.dp )
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onClickRetry()
                }
                .padding(8.dp),
            text = stringResource(id = R.string.try_it),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )*/
    }
}

@Composable
fun Result.NetworkError.ShowNetworkErrorSnakeBarUi(view: View) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(
            view, getErrorType(
                this.type
            )
        )
        Result.NetworkError.NoInternet -> snakeBarMessage(
            view,
            stringResource(id = R.string.error_no_internet)
        )
    }
}

@Composable
fun Result.NetworkError.ShowNetworkErrorSnakeBar(
    scaffoldState: ScaffoldState,
) {
    val errorMessage = when (this) {
        is Result.NetworkError.Generic -> getErrorType(this.type)
        Result.NetworkError.NoInternet -> stringResource(id = R.string.error_no_internet)
    }
    LaunchedEffect(key1 = null, block = {
        scaffoldState.snackbarHostState.showSnackbar(errorMessage)
    })

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