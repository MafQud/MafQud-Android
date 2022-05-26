package com.mafqud.android.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mafqud.android.R
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.ui.compose.CaseItem
import com.mafqud.android.ui.compose.DropDownItems
import com.mafqud.android.ui.picker.AgePickerDialog
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.HandlePagingError
import kotlinx.coroutines.flow.Flow

enum class CasesType {
    ALL,
    MISSING,
    FOUND
}

@Composable
fun HomeScreen(
    onTapClicked: (CasesType) -> Unit = {},
    cases: Flow<PagingData<CasesDataResponse.Case>>?,
    selectedTapState: CasesType
) {
    ColumnUi(
        Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HeadSearchUi(onTapClicked, selectedTapState)
        CasesUi(Modifier.weight(1f), cases)
    }
}

@Composable
fun HeadSearchUi(
    onTapClicked: (CasesType) -> Unit = {},
    selectedTapState: CasesType = CasesType.ALL
) {
    BoxUi(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        SearchUi(onTapClicked, selectedTapState)
    }
}


@Composable
private fun SearchUi(onTapClicked: (CasesType) -> Unit, selectedTapState: CasesType) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TapsUi(onTapClicked, selectedTapState)
        DropDownUi()
        SearchNameUi()
    }
}

@Composable
private fun SearchNameUi() {
    val searchName = remember {
        mutableStateOf("")
    }
    TextFieldUi(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions { /*onTypeListener(searchWords.value)*/ },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            TextUi(
                text = stringResource(id = R.string.search_title),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall
            )
        },
        shape = RoundedCornerShape(50),
        value = searchName.value,
        onValueChange = {
            searchName.value = it
            //onTypeListener(it)
        },
        singleLine = true,
        leadingIcon = {
            IconUi(
                imageVector = Icons.Outlined.Search,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.onSecondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun DropDownUi() {
    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }
        AgeTextPicker(
            Modifier
                .weight(1f)
                .height(30.dp)
        )
        DropDownItems(
            items = listOf("تاريخ التغيب"),
            selectedItemID = selectedItem,
            modifier = Modifier
                .weight(1f)
                .height(30.dp),
        )
        DropDownItems(
            items = listOf("المحافظة"),
            selectedItemID = selectedItem,
            modifier = Modifier
                .weight(1f)
                .height(30.dp),
        )


    }
}

@Composable
fun AgeTextPicker(modifier: Modifier) {
    val isOpened = remember {
        mutableStateOf(false)
    }
    val startSlider = remember {
        mutableStateOf(0f)
    }
    val endSlider = remember {
        mutableStateOf(20f)
    }
    BoxUi(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 4.dp)
            .clickable {
                isOpened.value = true
            },
        contentAlignment = Alignment.Center
    ) {
        val ageRange =
            startSlider.value.toInt().toString() + " : " + endSlider.value.toInt().toString()
        TextUi(
            modifier = Modifier.padding(start = 4.dp),
            text =
            stringResource(id = R.string.age) + " " + ageRange,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        ) // City name label
    }


    AgePickerDialog(
        startPickedAge = startSlider,
        endPickedAge = endSlider,
        isOpened = isOpened,
    )
}

@Composable
private fun TapsUi(onTapClicked: (CasesType) -> Unit, selectedTapState: CasesType) {
    RowUi {
        val selectedItem = remember {
            mutableStateOf(selectedTapState)
        }

        val activeTextColor = MaterialTheme.colorScheme.onSecondary
        val activeBackgroundColor = MaterialTheme.colorScheme.primary


        val disableTextColor = MaterialTheme.colorScheme.primary
        val disableBackgroundColor = MaterialTheme.colorScheme.onSecondary

        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .background(if (selectedItem.value == CasesType.ALL) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesType.ALL) {
                        selectedItem.value = CasesType.ALL
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.all),
                color = if (selectedItem.value == CasesType.ALL) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        //
        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RectangleShape
                )
                .background(if (selectedItem.value == CasesType.MISSING) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesType.MISSING) {
                        selectedItem.value = CasesType.MISSING
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.lost),
                color = if (selectedItem.value == CasesType.MISSING) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        //
        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                )
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(if (selectedItem.value == CasesType.FOUND) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesType.FOUND) {
                        selectedItem.value = CasesType.FOUND
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.found),
                color = if (selectedItem.value == CasesType.FOUND) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
private fun CasesUi(
    modifier: Modifier, cases: Flow<PagingData<CasesDataResponse.Case>>?,
    onCaseClicked: (CasesDataResponse.Case) -> Unit = {}
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
                        CaseItem(caseData = item, onCaseClicked = onCaseClicked)
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
fun EmptyCasesState(fillParentMaxSize: Modifier = Modifier) {
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
                painter = painterResource(id = R.drawable.ic_feed_empty),
                modifier = Modifier.size(290.dp, 190.dp)
            )

            TextUi(
                text = stringResource(id = R.string.empty_data),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
