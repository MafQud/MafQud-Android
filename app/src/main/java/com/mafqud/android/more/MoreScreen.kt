package com.mafqud.android.more

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mafqud.android.R
import com.mafqud.android.home.EmptyCasesState
import com.mafqud.android.reportedCases.UserCaseItem
import com.mafqud.android.reportedCases.models.ReportedCasesResponse
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.HandlePagingError
import kotlinx.coroutines.flow.Flow

@Composable
fun MoreScreen(
    onReportedClicked: () -> Unit,
    onAccountClicked: () -> Unit,
    onSettingClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    onPhonesClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    cases: Flow<PagingData<ReportedCasesResponse.UserCase>>?,
    onFoundCase: (ReportedCasesResponse.UserCase) -> Unit = {},
    onArchiveCase: (ReportedCasesResponse.UserCase) -> Unit = {},
) {
    ColumnUi {

        TitledAppBar(
            title = stringResource(id = R.string.title_more)
        )
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(start = 16.dp, end = 16.dp),

            ) {
            ColumnUi(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                /*.verticalScroll(rememberScrollState())*/,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                SpacerUi(modifier = Modifier.height(16.dp))
                ReportedItem(
                    title = stringResource(id = R.string.reported_people),
                    icon = R.drawable.ic_report,
                    onItemClicked = onReportedClicked,
                )
                DisplayUserCases(cases, onFoundCase, onArchiveCase)

                SpacerUi(modifier = Modifier.height(20.dp))
                //HorizontalLine()

                ColumnUi(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MoreItem(
                        title = stringResource(id = R.string.my_account),
                        icon = R.drawable.ic_account,
                        onItemClicked = onAccountClicked,

                        )
                    HorizontalLine()

                    // TODO settings item
                    /* MoreItem(
                         title = stringResource(id = R.string.settings),
                         icon = R.drawable.ic_setting,
                         onItemClicked = onSettingClicked,

                         )
                     HorizontalLine()*/

                    MoreItem(
                        title = stringResource(id = R.string.help),
                        icon = R.drawable.ic_info,
                        onItemClicked = onHelpClicked,

                        )
                    HorizontalLine()

                    MoreItem(
                        title = stringResource(id = R.string.phones),
                        icon = R.drawable.ic_phone,
                        onItemClicked = onPhonesClicked,
                    )
                    SpacerUi(modifier = Modifier.height(20.dp))

                    Logout(onConfirmClicked = onLogoutClicked)

                    SpacerUi(modifier = Modifier.height(16.dp))
                }

            }
        }
    }
}

@Composable
fun DisplayUserCases(
    cases: Flow<PagingData<ReportedCasesResponse.UserCase>>?,
    onFoundCase: (ReportedCasesResponse.UserCase) -> Unit,
    onArchiveCase: (ReportedCasesResponse.UserCase) -> Unit,
) {
    cases?.let {
        // Remember our own LazyListState
        val listState = rememberLazyListState()

        val lazyPagingItems = cases.collectAsLazyPagingItems()

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
                    EmptyCasesState((Modifier.size(150.dp)))
                }

            }

            items(lazyPagingItems) { item ->
                item?.let {
                    UserCaseItem(
                        caseData = item, onCaseClicked = null,
                        onFoundCase = onFoundCase,
                        onArchiveCase = onArchiveCase
                    )
                }
            }

            // for adding footer indicator for state
            item {
                HandlePagingError(
                    isSimpleErrorItems = true,
                    loadState = lazyPagingItems.loadState,
                    modifier = Modifier.size(150.dp),
                    onRetry = {
                        lazyPagingItems.retry()
                    }
                )
            }
        }

    }
}

@Composable
fun ReportedItem(title: String, icon: Int, onItemClicked: () -> Unit) {
    RowUi(
        modifier = Modifier
            .clickable(onClick = onItemClicked),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RowUi(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeftIcon(icon)
            TextUi(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

        }
        TextUi(
            text = stringResource(id = R.string.see_all),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

    }

}

@Composable
private fun Logout(onConfirmClicked: () -> Unit) {
    val isOpened = remember {
        mutableStateOf(false)
    }
    LogoutDialog(
        titleHead = stringResource(id = R.string.are_you_sure_logout),
        isOpened = isOpened, onConfirmClicked = {
            onConfirmClicked()
        }
    )

    RowUi(
        modifier = Modifier
            .clickable {
                isOpened.value = true
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconLogout(
            iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            iconSize = 24.dp
        )
        TextUi(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.log_out),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
    }

}

@Composable
@Preview
private fun MoreItem(
    title: String = "Report",
    @DrawableRes icon: Int = R.drawable.ic_report,
    onItemClicked: () -> Unit = {}
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeftIcon(icon)
        TextUi(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )

    }
}

@Composable
fun LeftIcon(
    @DrawableRes icon: Int,
) {
    IconUi(painter = painterResource(id = icon), tint = MaterialTheme.colorScheme.primary)

}

@Composable
fun HorizontalLine() {
    SpacerUi(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(2.dp)
            .alpha(0.05f)
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
    )
}
