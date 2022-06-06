package com.mafqud.android.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.mafqud.android.locations.MyGov
import com.mafqud.android.ui.compose.CaseItem
import com.mafqud.android.ui.compose.DropDownItems
import com.mafqud.android.ui.compose.IconAdd
import com.mafqud.android.ui.picker.AgePickerDialog
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.HandlePagingError
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.flow.Flow

enum class CasesTabType {
    ALL,
    MISSING,
    FOUND
}

@Composable
fun HomeScreen(
    onTapClicked: (CasesTabType) -> Unit = {},
    onRangeSelected: (AgeRange) -> Unit = {},
    onCaseClicked: (CasesDataResponse.Case) -> Unit = {},
    cases: Flow<PagingData<CasesDataResponse.Case>>?,
    selectedTapState: CasesTabType,
    ageRange: AgeRange?,
    searchedName: String?,
    onSearchTyping: (String) -> Unit = {},
    govs: List<MyGov>?,
    onGovSelected: (Int?) -> Unit = {},
    selectedGovId: Int?,
    isNoName: Boolean,
    onNoNameSelected: () -> Unit

) {
    ColumnUi(
        Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HeadSearchUi(
            onTapClicked, selectedTapState,
            ageRange, onRangeSelected,
            searchedName, onSearchTyping,
            govs, onGovSelected, selectedGovId,
            isNoName,
            onNoNameSelected
        )
        CasesUi(Modifier.weight(1f), cases, onCaseClicked)
    }
}

@Composable
fun HeadSearchUi(
    onTapClicked: (CasesTabType) -> Unit = {},
    selectedTapState: CasesTabType = CasesTabType.ALL,
    ageRange: AgeRange?,
    onRangeSelected: (AgeRange) -> Unit,
    searchedName: String?,
    onSearchTyping: (String) -> Unit,
    govs: List<MyGov>?,
    onGovSelected: (Int?) -> Unit,
    selectedGovId: Int?,
    isNoName: Boolean,
    onNoNameSelected: () -> Unit
) {
    BoxUi(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        SearchUi(
            //tap action
            onTapClicked,
            selectedTapState,
            // age filter
            ageRange,
            onRangeSelected,
            // name filter
            searchedName,
            onSearchTyping,
            isNoName,
            // gov filer
            govs,
            onGovSelected,
            selectedGovId,
            onNoNameSelected
        )
    }
}


@Composable
private fun SearchUi(
    onTapClicked: (CasesTabType) -> Unit,
    selectedTapState: CasesTabType,
    ageRange: AgeRange?,
    onRangeSelected: (AgeRange) -> Unit,
    searchedName: String?,
    onSearchTyping: (String) -> Unit,
    isNoName: Boolean,
    govs: List<MyGov>?,
    onGovSelected: (Int?) -> Unit,
    selectedGovId: Int?,
    onNoNameSelected: () -> Unit

) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TapsUi(onTapClicked, selectedTapState)
        DropDownUi(ageRange, onRangeSelected, govs, onGovSelected, selectedGovId)
        SearchNameUi(searchedName, onSearchTyping, isNoName,onNoNameSelected )
    }
}

@Composable
private fun SearchNameUi(
    searchedName: String?,
    onSearchTyping: (String) -> Unit,
    isNoName: Boolean,
    onNoNameSelected: () -> Unit
) {
    val mSearchName = remember {
        mutableStateOf(searchedName ?: "")
    }
    if (searchedName == null) {
        mSearchName.value = ""
    }


    BoxUi {
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
            value = mSearchName.value,
            onValueChange = {
                mSearchName.value = it
                onSearchTyping(it)
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
        IncludeNoNames(
            Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            isNoName,
            onNoNameSelected
        )
    }
}

@Composable
fun IncludeNoNames(modifier: Modifier, isSelected: Boolean, onNoNameSelected: () -> Unit) {

    val color = animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.green
        else MaterialTheme.colorScheme.green.copy(alpha = 0.4f)
    )
    BoxUi(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.value)
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onNoNameSelected()
            }
    ) {
        RowUi(Modifier.padding(4.dp)) {
            IconAdd()
            TextUi(
                text = stringResource(id = R.string.no_name_state),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun DropDownUi(
    ageRange: AgeRange?,
    onRangeSelected: (AgeRange) -> Unit,
    govs: List<MyGov>?,
    onGovSelected: (Int?) -> Unit,
    selectedGovId: Int?
) {

    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AgeTextPicker(
            Modifier
                .weight(1f)
                .height(30.dp),
            ageRange,
            onRangeSelected
        )
        DropDownItems(
            items = listOf("تاريخ التغيب"),
            modifier = Modifier
                .weight(1f)
                .height(30.dp),
        )


        // Govs
        val selectedItem = remember {
            mutableStateOf(selectedGovId.toString())
        }

        govs?.let {
            if (selectedGovId == null) {
                selectedItem.value = "-1"
            } else selectedItem.value = govs.find {
                return@find (it.id == selectedGovId)
            }?.name ?: "-1"

            DropDownItems(
                title = stringResource(id = R.string.gov),
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp),
                items = it.map {
                    return@map it.name ?: ""
                },
                selectedItemTitle = selectedItem,
                onSelectItem = {
                    val itemID = it.toIntOrNull() ?: -1
                    val govId = govs.getOrNull(itemID)?.id
                    onGovSelected(govId)

                }
            )
        }

    }
}

@Composable
fun AgeTextPicker(
    modifier: Modifier,
    ageRange: AgeRange?,
    onRangeSelected: (AgeRange) -> Unit
) {
    val isOpened = remember {
        mutableStateOf(false)
    }
    val startSlider = remember {
        mutableStateOf((ageRange?.start ?: 0).toFloat())
    }
    val endSlider = remember {
        mutableStateOf((ageRange?.end ?: 20).toFloat())
    }
    if (ageRange == null) {
        startSlider.value = 0F
        endSlider.value = 20F
    }
    LogMe.i("restAgeRange", startSlider.value.toString())
    LogMe.i("restAgeRange", endSlider.value.toString())

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
        val mAgeRange =
            startSlider.value.toInt().toString() + " : " + endSlider.value.toInt().toString()
        TextUi(
            modifier = Modifier.padding(start = 4.dp),
            text =
            stringResource(id = R.string.age) + " " + mAgeRange,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        ) // City name label
    }

    AgePickerDialog(
        startPickedAge = startSlider,
        endPickedAge = endSlider,
        isOpened = isOpened,
        onConfirmClicked = {
            onRangeSelected(
                AgeRange(
                    start = startSlider.value.toInt(),
                    end = endSlider.value.toInt()
                )
            )
        }
    )
}

@Composable
private fun TapsUi(onTapClicked: (CasesTabType) -> Unit, selectedTapState: CasesTabType) {
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
                .background(if (selectedItem.value == CasesTabType.ALL) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesTabType.ALL) {
                        selectedItem.value = CasesTabType.ALL
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.all),
                color = if (selectedItem.value == CasesTabType.ALL) activeTextColor else disableTextColor,
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
                .background(if (selectedItem.value == CasesTabType.MISSING) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesTabType.MISSING) {
                        selectedItem.value = CasesTabType.MISSING
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.lost),
                color = if (selectedItem.value == CasesTabType.MISSING) activeTextColor else disableTextColor,
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
                .background(if (selectedItem.value == CasesTabType.FOUND) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    if (selectedItem.value != CasesTabType.FOUND) {
                        selectedItem.value = CasesTabType.FOUND
                        onTapClicked(selectedItem.value)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = stringResource(id = R.string.found),
                color = if (selectedItem.value == CasesTabType.FOUND) activeTextColor else disableTextColor,
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
