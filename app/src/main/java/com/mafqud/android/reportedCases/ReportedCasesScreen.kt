package com.mafqud.android.reportedCases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

import com.mafqud.android.home.EmptyCasesState
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.ui.compose.CaseItem
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.LazyColumnUi
import com.mafqud.android.util.network.HandlePagingError
import kotlinx.coroutines.flow.Flow


@Composable
fun ReportedCasesScreen(
    cases: Flow<PagingData<CasesDataResponse.Case>>?
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
                if (lazyPagingItems.itemCount == 0 && finishedLoading) {
                    item {
                        EmptyCasesState((Modifier.fillParentMaxSize()))
                    }
                }

                items(lazyPagingItems) { item ->
                    item?.let {
                        CaseItem(caseData = item, onCaseClicked = null)
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
