package com.mafqud.android.notification

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mafqud.android.R
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.notification.models.NotificationAction
import com.mafqud.android.notification.models.NotificationIconType
import com.mafqud.android.notification.models.NotificationsResponse
import com.mafqud.android.report.CircleDot
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.dateFormat.fromFullDateToAnother
import com.mafqud.android.util.network.HandlePagingError
import kotlinx.coroutines.flow.Flow
import java.io.Serializable


@Keep
data class CaseModel(
    val caseId: Int = -1,
    val caseType: CaseType = CaseType.NONE
) : Serializable

@Keep
sealed class NotificationClickAction {
    data class Success(val caseModel: CaseModel) : NotificationClickAction()
    data class Failed(val caseModel: CaseModel) : NotificationClickAction()
    data class Details(val caseModel: CaseModel) : NotificationClickAction()
    object None : NotificationClickAction()
}


@Composable
@Preview
fun NotificationScreen(
    data: Flow<PagingData<NotificationsResponse.Notification>>? = null,
    onNotificationClicked: (NotificationClickAction, Int?) -> Unit = { it, _it -> },
) {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        data?.let {
            // Remember our own LazyListState
            val listState = rememberLazyListState()

            val lazyPagingItems = data.collectAsLazyPagingItems()

            val loadState = lazyPagingItems.loadState
            val finishedLoading =
                loadState.refresh !is LoadState.Loading &&
                        loadState.prepend !is LoadState.Loading &&
                        loadState.append !is LoadState.Loading &&
                        loadState.refresh !is LoadState.Error

            LazyColumnUi(
                state = listState,
                //modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                item {
                    SpacerUi(modifier = Modifier.height(8.dp))
                }

                if (lazyPagingItems.itemCount == 0 && finishedLoading) {
                    item {
                        EmptyNotificationState(Modifier.fillParentMaxSize())
                    }
                }

                items(lazyPagingItems) { item ->
                    item?.let {
                        NotificationItem(item, onNotificationClicked)
                    }
                }

                // for adding footer indicator for state

                item {
                    HandlePagingError(
                        loadState = lazyPagingItems.loadState,
                        modifier = Modifier.fillParentMaxSize(),
                        onRetry = {
                            lazyPagingItems.retry()
                        }
                    )
                }

            }

        }
    }
}

@Composable
fun EmptyNotificationState(fillParentMaxSize: Modifier) {
    BoxUi(
        modifier = fillParentMaxSize,
        contentAlignment = Alignment.Center
    ) {
        ColumnUi(
            /* modifier = Modifier
                 .verticalScroll(rememberScrollState()),*/
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageUi(
                painter = painterResource(id = R.drawable.ic_notification_bell),
                modifier = Modifier.size(265.dp, 220.dp)
            )

            TextUi(
                text = stringResource(id = R.string.empty_notificaction),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotificationsResponse.Notification,
    onNotificationClicked: (NotificationClickAction, Int?) -> Unit
) {
    BoxUi(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                when (notification.getAction()) {
                    NotificationAction.MATCHES -> {
                        onNotificationClicked(
                            NotificationClickAction.Success(
                                CaseModel(
                                    caseId = notification.caseId,
                                    caseType = notification.getCaseType()
                                )
                            )
                        , notification.id
                        )
                    }
                    NotificationAction.PUBLISHED -> {
                        onNotificationClicked(
                            NotificationClickAction.Failed(
                                CaseModel(
                                    caseId = notification.caseId,
                                    caseType = notification.getCaseType()
                                )
                            )
                        , notification.id
                        )
                    }
                    NotificationAction.DETAILS -> {
                        onNotificationClicked(
                            NotificationClickAction.Details(
                                CaseModel(
                                    caseId = notification.caseId,
                                    caseType = notification.getCaseType()
                                )
                            )
                            , notification.id
                        )
                    }
                    NotificationAction.NONE -> {
                        onNotificationClicked(NotificationClickAction.None, notification.id)
                    }
                }
            }
            .padding(8.dp)
    ) {
        RowUi(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            NotificationUnreadDot(Modifier.alignByBaseline(),notification)

            ColumnUi(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextUi(
                    text = notification.body ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge
                )

                //date
                val date = fromFullDateToAnother(notification.createdAt)
                TextUi(
                    text = date,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            // icon
            val iconRes = when (notification.iconType) {
                NotificationIconType.SUCCESS -> R.drawable.ic_state_success
                NotificationIconType.WARNING -> R.drawable.ic_warning
                NotificationIconType.INFO -> R.drawable.ic_info
                NotificationIconType.ERROR -> R.drawable.ic_error
                NotificationIconType.NONE -> null
            }

            iconRes?.let {
                IconUi(
                    painter = painterResource(id = iconRes),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}

@Composable
fun NotificationUnreadDot(modifier: Modifier, notification: NotificationsResponse.Notification) {
    if (notification.readAt == null) {
        val dotColor: Color? = when (notification.iconType) {
            NotificationIconType.SUCCESS -> MaterialTheme.colorScheme.green
            NotificationIconType.WARNING -> MaterialTheme.colorScheme.yellow
            NotificationIconType.INFO -> MaterialTheme.colorScheme.primary
            NotificationIconType.ERROR -> MaterialTheme.colorScheme.error
            NotificationIconType.NONE -> null
        }

        BoxUi(
            modifier = modifier
                .padding(end = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            dotColor?.let {
                CircleDot(color = dotColor)
            }
        }
    }
}

