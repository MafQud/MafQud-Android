package com.mafqud.android.reportedCases

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mafqud.android.R

import com.mafqud.android.home.EmptyCasesState
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.reportedCases.models.ReportedCasesResponse
import com.mafqud.android.reportedCases.models.UserCaseState
import com.mafqud.android.ui.compose.IconMap
import com.mafqud.android.ui.compose.UserPhoto
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.HandlePagingError
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.flow.Flow


@Composable
fun ReportedCasesScreen(
    cases: Flow<PagingData<ReportedCasesResponse.UserCase>>?
) {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopCenter
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
                item {
                    SpacerUi(modifier = Modifier.height(8.dp))
                }
                if (lazyPagingItems.itemCount == 0 && finishedLoading) {
                    item {
                        EmptyCasesState((Modifier.fillParentMaxSize()))
                    }
                }

                items(lazyPagingItems) { item ->
                    item?.let {
                        UserCaseItem(caseData = item, onCaseClicked = null)
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
@Preview
fun UserCaseItem(
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
    caseData: ReportedCasesResponse.UserCase? = null,
    onCaseClicked: ((ReportedCasesResponse.UserCase) -> Unit)? = {},
) {


    caseData?.let { case ->
        BoxUi(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .clickable {
                    if (onCaseClicked != null) {
                        onCaseClicked(case)
                    }
                }
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            RowUi(
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Top
            ) {
                LogMe.i("case.thumbnail", case.thumbnail.toString())
                UserPhoto(imageUrl = case.thumbnail ?: "")
                ColumnUi(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // name
                    RowUi(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        ColumnUi(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            TextUi(
                                text = case.name ?: stringResource(id = com.mafqud.android.R.string.no_name),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )

                            //address
                            RowUi(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconMap()
                                TextUi(
                                    text = case.getFullAddress(),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }

                        // case state
                        when (case.getCaseState()) {
                            UserCaseState.ACTIVE -> {
                                UpdateStateButton()
                            }
                            UserCaseState.FINISHED -> {
                                SuccessToFound()
                            }
                            UserCaseState.ARCHIVED -> {
                                ArchiveState()
                            }
                            UserCaseState.PENDING -> {
                                PendingState()
                            }
                            UserCaseState.NONE -> {
                                NoneState()
                            }
                        }

                    }

                    // type
                    val type = when (case.getCaseType()) {
                        CaseType.FOUND -> stringResource(id = R.string.found)
                        CaseType.MISSING -> stringResource(id = R.string.lost)
                        CaseType.NONE -> stringResource(id = R.string.none)
                    }
                    TextUi(
                        text = type,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )

                    SpacerUi(modifier = Modifier.height(8.dp))
                    //date
                    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextUi(
                            text = stringResource(id = R.string.lost_date),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall
                        )
                        TextUi(
                            text = case.lastSeen ?: "",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun NoneState() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(18.dp)) {
            IconUi(
                painter = painterResource(id = R.drawable.ic_info),
                modifier = Modifier.size(18.dp),
            )
        }
        TextUi(
            text = stringResource(id = R.string.no_state),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
fun ArchiveState() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(18.dp)) {
            IconUi(
                painter = painterResource(id = R.drawable.ic_archive),
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
        TextUi(
            text = stringResource(id = R.string.archive_state),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
fun PendingState() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(18.dp)) {
            IconUi(
                painter = painterResource(id = R.drawable.ic_process),
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.yellow
            )
        }
        TextUi(
            text = stringResource(id = R.string.process_images),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
fun SuccessToFound() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(18.dp)) {
            IconUi(
                painter = painterResource(id = R.drawable.ic_state_success),
                modifier = Modifier.size(18.dp)
            )
        }
        TextUi(
            text = stringResource(id = R.string.success_to_found),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.green
        )

    }
}

@Composable
fun UpdateStateButton() {
    BoxUi(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.error)
            .clickable {
            }.padding(8.dp),
    ) {
        TextUi(
            // modifier = Modifier.padding(12.dp),
            text = stringResource(id = R.string.update_state),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

