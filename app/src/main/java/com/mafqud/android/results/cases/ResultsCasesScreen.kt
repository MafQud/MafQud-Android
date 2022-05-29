package com.mafqud.android.results.cases

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mafqud.android.R
import com.mafqud.android.home.EmptyCasesState
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.notification.CaseModel
import com.mafqud.android.results.cases.models.CasesMatchesResponse
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.IconMap
import com.mafqud.android.ui.compose.UserPhoto
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.HandlePagingError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
@Preview
fun ResultsCasesScreen(
    cases: Flow<PagingData<CasesMatchesResponse.CaseMatch>>? = emptyFlow(),
    caseModel: CaseModel? = CaseModel(),
    onNotFoundButton: () -> Unit = {},
    onCaseClicked: (CasesMatchesResponse.CaseMatch) -> Unit = {},
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
            .padding(horizontal = 16.dp)
    ) {
        ColumnUi(
            Modifier
                .background(
                    MaterialTheme.colorScheme.onPrimary
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BodyUi(cases, Modifier.weight(1f), onCaseClicked)
        }

        val bottomTitle = when (caseModel?.caseType) {
            CaseType.MISSING ->
                stringResource(id = R.string.title_losts_not_found)
            CaseType.FOUND ->
                stringResource(id = R.string.title_founds_not_found)
            else -> {
                ""
            }
        }

        BoxUi(
            Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
        ) {
            ButtonAuth(
                title = bottomTitle, backgroundColor = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.onPrimary
            ) {
                onNotFoundButton()
            }

        }
    }

}

@Composable
private fun BodyUi(
    cases: Flow<PagingData<CasesMatchesResponse.CaseMatch>>?,
    modifier: Modifier,
    onCaseClicked: (CasesMatchesResponse.CaseMatch) -> Unit
) {
    BoxUi(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary),
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                        CaseMatchItem(caseData = item, onCaseClicked = onCaseClicked)
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
fun CaseMatchItem(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    caseData: CasesMatchesResponse.CaseMatch? = null,
    onCaseClicked: (CasesMatchesResponse.CaseMatch) -> Unit = {},
) {


    caseData?.case?.let { case ->
        BoxUi(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .clickable {
                    if (onCaseClicked != null) {
                        onCaseClicked(caseData)
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
                                text = case.name ?: stringResource(id = R.string.no_name),
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

                        BoxUi(
                            Modifier
                                .size(70.dp, 30.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.error)
                                .clickable {
                                    if (onCaseClicked != null) {
                                        onCaseClicked(caseData)
                                    }
                                },
                        ) {
                            TextUi(
                                // modifier = Modifier.padding(12.dp),
                                text = stringResource(id = R.string.more),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    // state
                    val type = when(case.getCaseType()) {
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
