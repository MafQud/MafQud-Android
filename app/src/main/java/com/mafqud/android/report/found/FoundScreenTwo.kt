package com.mafqud.android.report.found

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.mafqud.android.R
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.locations.MyCity
import com.mafqud.android.locations.MyGov
import com.mafqud.android.report.lost.*
import com.mafqud.android.report.uploading.models.CreateCaseBody
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.picker.datePicker
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


val ages = (1..100)
val genders = listOf(Gender.NONE, Gender.MALE, Gender.FEMALE)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FragmentActivity.FoundScreenTwo(
    onBack: () -> Unit, isDialogOpened: MutableState<Boolean>,
    govs: List<MyGov>? = emptyList(),
    cities: List<MyCity>? = emptyList(),
    onGovSelected: (Int) -> Unit = {},
    onNext: (CreateCaseBody) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrollToPosition by remember { mutableStateOf(0F) }

    /**
     * Ui data
     */
    val selectedGender = remember {
        mutableStateOf(Gender.NONE)
    }

    val foundName = remember {
        mutableStateOf("")
    }

    val description = remember {
        mutableStateOf("")
    }
    val selectedAge = remember {
        mutableStateOf("")
    }

    val selectedDate: MutableState<String?> = remember {
        mutableStateOf(null)
    }

    // location info
    val selectedGovId = remember {
        mutableStateOf(-1)
    }
    val selectedCityId = remember {
        mutableStateOf(-1)
    }


    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 16.dp, end = 16.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            SpacerUi(modifier = Modifier.height(16.dp))
            HeaderTwo()
            FoundName(foundName)
            FoundGenderAndAge(selectedGender, selectedAge)
            FoundDate(selectedDate, this@FoundScreenTwo)
            FoundDescription(description)
            LocationForm(
                govs,
                cities,
                onGovSelected,
                selectedGovId,
                selectedCityId
            )
            //PhoneReportForm(phone)
            BoxUi(modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    scrollToPosition = coordinates.positionInParent().y
                }) {
                ButtonAuth(
                    enabled = true,
                    title = stringResource(id = R.string.search_founds),
                    onClick = {
                        if (isLocationValid(
                                selectedGovId.value,
                                selectedCityId.value
                            )
                        ) {
                            onNext(
                                CreateCaseBody(
                                    details = CreateCaseBody.Details(
                                        age = getUserAge(selectedAge.value),
                                        name = getName(foundName.value),
                                        description = getDes(description.value),
                                        gender = getGender(selectedGender),
                                        lastSeen = selectedDate.value,
                                        location = getSelectedLocation(
                                            selectedGovId,
                                            selectedCityId
                                        )
                                    ),
                                    caseType = CaseType.FOUND
                                )
                            )
                        } else {
                            showToast(getString(R.string.error_location))
                        }
                    })
            }

            SpacerUi(modifier = Modifier.height(16.dp))

        }
        val isArrowBottomVisible = scrollState.value <= scrollState.maxValue / 2
        AnimatedVisibility(
            visible = isArrowBottomVisible,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            BoxUi(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = 12.dp,
                        end = 12.dp
                    ), contentAlignment = Alignment.BottomEnd
            ) {
                IconDown {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollToPosition.roundToInt())
                    }
                }
            }
        }

    }
    DismissDialog(isOpened = isDialogOpened, onConfirmClicked = {
        onBack()
    })

}

@Composable
fun FoundDescription(description: MutableState<String>) {
    val isTextError = remember {
        mutableStateOf(false)
    }
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.found_des),
            style = MaterialTheme.typography.titleMedium

        )

        TextFieldDescription(description, isTextError)
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_des_title),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
        )

    }
}

@Composable
fun FoundDate(selectedDate: MutableState<String?>, activity: FragmentActivity) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        val displayedDate = remember {
            mutableStateOf("")
        }
        val datePicker = datePicker(updatedDate = { global, display ->
            selectedDate.value = global
            displayedDate.value = display
        })
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_date),
            style = MaterialTheme.typography.titleMedium

        )
        DateUi(dataString = displayedDate.value,
            onClick = {
                datePicker.show(activity.supportFragmentManager, "")
            })
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_date_title),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
        )
    }
}


@Composable
private fun FoundGenderAndAge(gender: MutableState<Gender>, age: MutableState<String>) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.gender_age),
            style = MaterialTheme.typography.titleMedium

        )
        RowUi(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val agesList = ages.toList().map {
                it.toString()
            }.toMutableList().apply {
                add(0, stringResource(id = R.string.age))
            }

            DropDownGender(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                items = genders.map {
                    when (it) {
                        Gender.MALE -> stringResource(id = R.string.male)
                        Gender.FEMALE -> stringResource(id = R.string.female)
                        Gender.NONE -> stringResource(id = R.string.gender)
                    }
                },
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onSelectItem = {
                    gender.value = it
                }
            )

            TextFieldAge(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                age = age
            )

        }
    }
}

@Composable
private fun FoundName(foundName: MutableState<String>) {

    val isNameError = remember {
        mutableStateOf(false)
    }

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.found_name),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldName(stringResource(id = R.string.example_name), foundName, isNameError)
    }
}

@Composable
private fun HeaderTwo() {
    IconUi(
        modifier = Modifier.size(77.dp),
        painter = painterResource(id = R.drawable.ic_info),
        tint = MaterialTheme.colorScheme.primary
    )
    TextUi(
        text = stringResource(id = R.string.insert_more_found),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LocationForm(
    govs: List<MyGov>?,
    cities: List<MyCity>?,
    onGovSelected: (Int) -> Unit,
    selectedGovId: MutableState<Int>,
    selectedCityId: MutableState<Int>,
) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.address_founded),
            style = MaterialTheme.typography.titleMedium

        )
        RowUi(
            modifier = Modifier.animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            govs?.let {
                DropDownItems(
                    title = stringResource(id = R.string.gov),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    items = it.map {
                        return@map it.name ?: ""
                    },
                    iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    onSelectItem = {
                        val itemID = it.toIntOrNull() ?: -1
                        val govId = govs.getOrNull(itemID)?.id ?: -1
                        selectedGovId.value = govId
                        onGovSelected(govId)
                        LogMe.i("DropDownItems", "selected $it")
                    }
                )
            }

            cities?.let {
                DropDownItems(
                    title = stringResource(id = R.string.city),
                    items = it.map {
                        return@map it.name ?: ""
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    onSelectItem = {
                        val itemID = it.toIntOrNull() ?: -1
                        val cityId = cities.getOrNull(itemID)?.id ?: -1
                        selectedCityId.value = cityId
                        LogMe.i("DropDownItems", "selected $it")
                    }
                )
            }


        }
    }
}

