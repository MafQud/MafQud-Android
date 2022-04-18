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
import com.mafqud.android.report.PhoneReportForm
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.picker.datePicker
import com.mafqud.android.ui.theme.*
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
fun FragmentActivity.LostScreenTwo(onNext: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrollToPosition by remember { mutableStateOf(0F) }

    /**
     * Ui data
     */
    val selectedgender = remember {
        mutableStateOf(Gender.NONE)
    }

    val selectedAge = remember {
        mutableStateOf("")
    }

    val selectedDate = remember {
        mutableStateOf("")
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
            LostName()
            LostGenderAndAge(selectedgender, selectedAge)
            LostDate(selectedDate, this@LostScreenTwo)
            LostDescription()
            LocationForm()
            PhoneReportForm()
            BoxUi(modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    scrollToPosition = coordinates.positionInParent().y
                }) {
                ButtonAuth(
                    enabled = true,
                    title = stringResource(id = R.string.search_losts),
                    onClick = {
                        onNext()
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
}

@Composable
fun LostDescription() {
    val description = remember {
        mutableStateOf("")
    }

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
fun LostDate(selectedDate: MutableState<String>, activity: FragmentActivity) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        val datePicker = datePicker(updatedDate = { long, string ->
            selectedDate.value = string
        })
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_date),
            style = MaterialTheme.typography.titleMedium

        )
        DateUi(dataString = selectedDate.value,
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

            DropDownItems(
                items = agesList,
                selectedItemID = age,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )


        }
    }
}

@Composable
private fun LostName() {
    val fullName = remember {
        mutableStateOf("")
    }

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
        TextFieldName(stringResource(id = R.string.example_name), fullName, isNameError)
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
private fun LocationForm() {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.address_lost_where),
            style = MaterialTheme.typography.titleMedium

        )
        RowUi(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DropDownItems(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                items = listOf("Gov"),
                selectedItemID = selectedItem,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            DropDownItems(
                items = listOf("City"),
                selectedItemID = selectedItem,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}
