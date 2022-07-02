package com.mafqud.android.report.lost

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.mafqud.android.R
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.locations.MyCity
import com.mafqud.android.locations.MyGov
import com.mafqud.android.report.PhoneReportForm
import com.mafqud.android.report.uploading.models.CreateCaseBody
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.other.showToast
import com.mafqud.android.ui.picker.datePicker
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


enum class Gender {
    MALE, FEMALE, NONE
}

val ages = (1..100)
val genders = listOf(Gender.NONE, Gender.MALE, Gender.FEMALE)

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun FragmentActivity.LostScreenTwo(
    onNext: (CreateCaseBody) -> Unit = {},
    onBack: () -> Unit = {},
    isDialogOpened: MutableState<Boolean> = mutableStateOf(true),
    govs: List<MyGov>? = emptyList(),
    cities: List<MyCity>? = emptyList(),
    onGovSelected: (Int) -> Unit = {},
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

    val lostName = remember {
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
    val phone = remember {
        mutableStateOf("")
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
            LostName(lostName)
            LostGenderAndAge(selectedGender, selectedAge)
            LostDate(selectedDate, this@LostScreenTwo)
            LostDescription(description)
            LocationForm(govs, cities, onGovSelected, selectedGovId, selectedCityId)
            //PhoneReportForm(phone)
            BoxUi(modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    scrollToPosition = coordinates.positionInParent().y
                }) {
                ButtonAuth(
                    enabled = true,
                    title = stringResource(id = R.string.search_losts),
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
                                        name = getName(lostName.value),
                                        description = getDes(description.value),
                                        gender = getGender(selectedGender),
                                        lastSeen = selectedDate.value,
                                        location = getSelectedLocation(
                                            selectedGovId,
                                            selectedCityId
                                        )
                                    ),
                                    caseType = CaseType.MISSING
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

fun isLocationValid(gov: Int, city: Int): Boolean {
    return !(city == -1 && gov != -1)
}

fun getSelectedLocation(
    selectedGovId: MutableState<Int>,
    selectedCityId: MutableState<Int>
): CreateCaseBody.Details.Location? {
    if (selectedGovId.value == -1 && selectedCityId.value == -1)
        return null

    return CreateCaseBody.Details.Location(
        gov = selectedGovId.value.toString(),
        city = selectedCityId.value.toString(),
        // TODO pass user address
        address = "TODO"
    )
}

fun getName(name: String): String? {
    return if (name.trim().isEmpty()) null else name.trim()
}

fun getDes(description: String): String? {
    return if (description.trim().isEmpty()) null else description.trim()
}

fun getUserAge(value: String): Int? {
    return value.trim().toIntOrNull()
}

fun getGender(selectedGender: MutableState<Gender>): String? {
    return when (selectedGender.value) {
        Gender.MALE -> "M"
        Gender.FEMALE -> "F"
        Gender.NONE -> null
    }
}

@Composable
fun LostDescription(description: MutableState<String>) {
    val isTextError = remember {
        mutableStateOf(false)
    }
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_des),
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
fun LostDate(selectedDate: MutableState<String?>, activity: FragmentActivity) {
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
private fun LostGenderAndAge(gender: MutableState<Gender>, age: MutableState<String>) {
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
private fun LostName(lostName: MutableState<String>) {
    val isNameError = remember {
        mutableStateOf(false)
    }

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_name),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldName(stringResource(id = R.string.example_name), lostName, isNameError)
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
        text = stringResource(id = R.string.insert_more),
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
            text = stringResource(id = R.string.address_lost),
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
