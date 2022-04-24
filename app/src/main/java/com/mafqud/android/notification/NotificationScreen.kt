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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.HandlePagingError
import kotlinx.coroutines.flow.Flow


@Keep
enum class NotificationType {
    SUCCESS_FINDING_LOST,
    SUCCESS_FINDING_FOUND,
    FAILED_LOST,
    FAILED_FOUND,
    OTHER,
    NONE
}


@Composable
@Preview
fun NotificationScreen(
    data: Flow<PagingData<NotificationResponse.Data>>? = null,
    onNotificationClicked: (NotificationResponse.Data, NotificationType) -> Unit = { it, _it -> },
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
    item: NotificationResponse.Data,
    onSuccessNotificationClicked: (NotificationResponse.Data, NotificationType) -> Unit
) {
    BoxUi(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                // TODO change NotificationType
                if (listOf(false, true).random()) {
                    onSuccessNotificationClicked(item, NotificationType.SUCCESS_FINDING_FOUND)
                } else {
                    onSuccessNotificationClicked(item, NotificationType.FAILED_FOUND)
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
            ColumnUi(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextUi(
                    text = "تم نشر بيانات المعثور عليه بنجاح انتظر منا اشعار اخر في حين الوصول لأي نتائج",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge
                )

                //date
                TextUi(
                    text = "March,13, 2022 at 3:15 PM",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            // icon
            IconUi(
                painter = painterResource(id = R.drawable.ic_state_success),
                modifier = Modifier.size(24.dp)
            )

        }
    }

}

