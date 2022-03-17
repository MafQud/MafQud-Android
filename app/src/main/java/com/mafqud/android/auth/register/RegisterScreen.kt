package com.mafqud.android.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.theme.*


enum class StepCount {
    One,
    Two,
    Three,
    Four,
    Five
}


@Composable
fun RegisterScreen(onBackPressed: () -> Unit) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ColumnUi(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val activeStep = remember {
                mutableStateOf(StepCount.One)
            }

            HeadItem(activeStep, Modifier.weight(1f), onBackPressed)

            ColumnUi(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BoxUi(
                    Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    ColumnUi {
                        SpacerUi(modifier = Modifier.height(50.dp))
                        // display the needed view
                        when (activeStep.value) {
                            StepCount.One -> EmailForm(activeStep)
                            StepCount.Two -> PhoneForm(activeStep)
                            StepCount.Three -> NameForm(activeStep)
                            StepCount.Four -> LocationForm(activeStep)
                            StepCount.Five -> PassWordForm(activeStep)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PassWordForm(activeStep: MutableState<StepCount>) {
    ButtonAuth(title = stringResource(id = R.string.next), onClick = {

    })
}

@Composable
private fun LocationForm(activeStep: MutableState<StepCount>) {
    ButtonAuth(title = stringResource(id = R.string.next), onClick = {
        activeStep.value = StepCount.Five
    })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NameForm(activeStep: MutableState<StepCount>) {
    val firstName = remember {
        mutableStateOf("")
    }

    val phone = remember {
        mutableStateOf("")
    }
    val isPhoneError = remember {
        mutableStateOf(false)
    }
    val (focusRequester) = FocusRequester.createRefs()

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.full_name),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldName(stringResource(id = R.string.example_name), firstName)

        SpacerUi(modifier = Modifier.height(8.dp))

        //phone
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_phone_num),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldPhone(phone, isPhoneError, focusRequester)

        SpacerUi(modifier = Modifier.height(8.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            activeStep.value = StepCount.Four
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PhoneForm(activeStep: MutableState<StepCount>) {
    val phone = remember {
        mutableStateOf("")
    }
    val isPhoneError = remember {
        mutableStateOf(false)
    }
    val (focusRequester) = FocusRequester.createRefs()

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_phone_num),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldPhone(phone, isPhoneError, focusRequester)
        SpacerUi(modifier = Modifier.height(20.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            activeStep.value = StepCount.Three
        })
    }
}

@Composable
private fun EmailForm(activeStep: MutableState<StepCount>) {
    val email = remember {
        mutableStateOf("")
    }
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_email),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldEmail(email)
        SpacerUi(modifier = Modifier.height(20.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            activeStep.value = StepCount.Two
        })
    }
}

@Composable
private fun HeadItem(
    activeStep: MutableState<StepCount>,
    weight: Modifier,
    onBackPressed: () -> Unit
) {
    ColumnUi(
        modifier = weight
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val stepsList = listOf(
            StepCount.One,
            StepCount.Two,
            StepCount.Three,
            StepCount.Four,
            StepCount.Five,
        )
        // back arrow
        BoxUi(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconBack(iconColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                onClick = {
                    onBackPressed()
                })
        }
        TextUi(
            modifier = Modifier
                .padding(top = 12.dp)
                .weight(1f),
            text = stringResource(id = R.string.create_account),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium
        )

        BoxUi(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            RowUi(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in stepsList.indices) {
                    StepItem(index, stepsList.get(index), activeStep.value)
                }
            }

        }
    }
}

@Composable
private fun StepItem(index: Int, current: StepCount, active: StepCount) {
    val isActive = when (active) {
        StepCount.One -> current == StepCount.One
        StepCount.Two -> (current == StepCount.One || current == StepCount.Two)
        StepCount.Three -> (current == StepCount.One || current == StepCount.Two || current == StepCount.Three)
        StepCount.Four
        -> (current == StepCount.One || current == StepCount.Two || current == StepCount.Three || current == StepCount.Four)
        StepCount.Five
        -> (current == StepCount.One || current == StepCount.Two || current == StepCount.Three || current == StepCount.Four || current == StepCount.Five)
    }
    //val isActive = current == active
    var alpha = 1f
    val color = if (isActive) {
        MaterialTheme.colorScheme.error
    } else {
        alpha = 0.6f
        MaterialTheme.colorScheme.onPrimary
    }

    val step = index + 1
    ColumnUi(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextUi(
            modifier = Modifier.alpha(alpha),
            text = step.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall
        )

        BoxUi(
            modifier = Modifier
                .size(50.dp, 12.dp)
                .clip(RoundedCornerShape(50))
                .alpha(alpha)
                .background(color)
        ) {

        }


    }
}