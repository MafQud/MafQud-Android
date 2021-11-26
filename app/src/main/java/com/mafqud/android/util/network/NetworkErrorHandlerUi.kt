package com.mafqud.android.util.network

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mafqud.android.R

import com.mafqud.android.ui.other.snakeBarMessage
import com.mafqud.android.ui.status.error.NoInternet

@Composable
fun Result.NetworkError.ShowNetworkErrorUi(onTryBtnClicked: () -> Unit, view: View) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(view, this.error.message)
        Result.NetworkError.NoInternet -> NoInternet(onTryBtnClicked)
    }
}


@Composable
fun Result.NetworkError.ShowNetworkErrorSnakeBarUi(view: View) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(view, this.error.message)
        Result.NetworkError.NoInternet -> snakeBarMessage(
            view,
            stringResource(id = R.string.error_no_internet)
        )
    }
}

fun Result.NetworkError.ShowNetworkErrorSnakeBarUi(view: View, context: Context) {
    when (this) {
        is Result.NetworkError.Generic -> snakeBarMessage(view, this.error.message)
        Result.NetworkError.NoInternet -> snakeBarMessage(
            view,
            context.getString(R.string.error_no_internet)
        )
    }
}