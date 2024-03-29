package com.mafqud.android.auth.register

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.locations.MyCity
import com.mafqud.android.locations.MyGov
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.other.TimerUi
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.validation.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class StepCount {
    One,
    Two,
    Three,
    Four,
    Five
}


@Composable
fun RegisterScreen(
    scaffoldState: ScaffoldState,
    stateValue: RegisterViewState,
    otpState: MutableState<String>,
    onStepOne: (String) -> Unit,
    onStepTwo: (String) -> Unit,
    onStepThree: (String) -> Unit,
    onStepFour: (Int, Int) -> Unit,
    onStepFive: (String) -> Unit,
    onBackPressed: () -> Unit,
    onSuccessRegister: () -> Unit = {},
    sendVerificationCode: (String) -> Unit = { _it -> },
    onGovSelected: (Int) -> Unit,
) {
    /**
     * ui states
     *
     */

    LogMe.i("phonee", stateValue.phone.toString())
    LoadingDialog(stateValue.isLoading)

    if (stateValue.isValidPhone != null) {
        if (stateValue.isValidPhone) {
            if (stateValue.phone != null) {
                sendVerificationCode(stateValue.phone)
            }

        } else {
            val notValidNum = stringResource(id = R.string.error_phone_exists)
            LaunchedEffect(key1 = null, block = {
                scaffoldState.snackbarHostState.showSnackbar(notValidNum)
            })
        }
    }

    /*if (stateValue.isValidEmail) {
        activeStep.value = StepCount.Four
    }*/


    if (stateValue.isSuccess) {
        onSuccessRegister()
    }

    if (stateValue.networkError != null) {
        stateValue.networkError.ShowNetworkErrorSnakeBar(
            scaffoldState = scaffoldState
        )
    }
    /**
     * ui data
     */
    // create variable for isTimerRunning
    val isTimerRunning = remember {
        mutableStateOf(false)
    }

    // create variable for current time
    val currentTime = remember {
        mutableStateOf(120L)
    }

    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ColumnUi(
            modifier = Modifier
                .fillMaxSize()
        ) {

            HeadItem(stateValue.stepCount, Modifier.weight(1f), onBackPressed)

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
                        when (stateValue.stepCount) {
                            StepCount.One -> PhoneForm(stateValue.phone ?: "", onStepOne)

                            StepCount.Two -> {
                                isTimerRunning.value = true
                                OTPForm(otpState, isTimerRunning, currentTime, onStepTwo)
                            }

                            StepCount.Three -> NameAndEmailForm(stateValue.name ?: "", onStepThree)

                            StepCount.Four -> LocationForm(
                                onStepFour,
                                onGovSelected,
                                stateValue.govs,
                                stateValue.cities,
                            )

                            StepCount.Five -> PassWordForm(
                                onStepFive
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OTPForm(
    otpState: MutableState<String>,
    isTimerRunning: MutableState<Boolean>,
    currentTime: MutableState<Long>,
    onClicked: (String) -> Unit
) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_otp),
            style = MaterialTheme.typography.titleMedium
        )

        val otpOPState = remember {
            mutableStateOf("")
        }
        val isEnabled = remember {
            mutableStateOf(false)
        }

        OTPCommon(code = otpState.value, onFilled = {
            otpOPState.value = it
            isEnabled.value = otpOPState.value.length == 6
        })


        TimerUi(isTimerRunning = isTimerRunning, currentTime = currentTime)


        SpacerUi(modifier = Modifier.height(20.dp))

        ButtonAuth(
            enabled = isEnabled.value,
            title = stringResource(id = R.string.next),
            onClick = {
                if (otpOPState.value.length == 6) {
                    onClicked(otpOPState.value)
                }

            })
    }
}

private fun gettingOTP(onGetOTP: (String) -> Unit) {
    GlobalScope.launch {
        // simulate getting otp from server
        //onGetOTP("")
        //Toast.makeText(requireContext(), "Sending OTP", Toast.LENGTH_SHORT).show()
        val otp = getFakeOTP()
        // pass otp to remember state
        onGetOTP(otp)
    }
}

private suspend fun getFakeOTP(): String {
    delay(1200)
    return "khda"
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PassWordForm(onClicked: (String) -> Unit) {

    val password = remember {
        mutableStateOf("")
    }

    val passwordConfirm = remember {
        mutableStateOf("")
    }

    val isPasswordError = remember { mutableStateOf(PasswordError()) }

    val (focusRequester) = FocusRequester.createRefs()

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.password),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldPassword(password, isPasswordError, focusRequester)

        SpacerUi(modifier = Modifier.height(8.dp))

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.password_confirm),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldPassword(passwordConfirm, isPasswordError, focusRequester)

        SpacerUi(modifier = Modifier.height(8.dp))
        ButtonAuth(title = stringResource(id = R.string.register), onClick = {
            // first validate data
            validatePassAndConfirm(
                password1 = password.value,
                password2 = passwordConfirm.value,
                isPasswordError = isPasswordError,
                onValidationSuccess = { pass ->
                    // fire button click
                    onClicked(pass)
                }
            )

        })
    }
}

@Composable
private fun LocationForm(
    onClicked: (Int, Int) -> Unit,
    onGovSelected: (Int) -> Unit,
    govs: List<MyGov>?,
    cities: List<MyCity>?,
) {
    val selectedGovId = remember {
        mutableStateOf(-1)
    }
    val selectedCityId = remember {
        mutableStateOf(-1)
    }
    val isLocationError = remember {
        mutableStateOf(false)
    }
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.address),
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
                        isLocationError.value = false
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
                        isLocationError.value = false
                        val itemID = it.toIntOrNull() ?: -1
                        val cityId = cities.getOrNull(itemID)?.id ?: -1
                        selectedCityId.value = cityId
                        LogMe.i("DropDownItems", "selected $it")
                    }
                )
            }
        }
       if (isLocationError.value) {
           //error message
           TextUi(
               text = stringResource(id = R.string.error_location),
               textAlign = TextAlign.Start,
               color = MaterialTheme.colorScheme.error,
               style = MaterialTheme.typography.titleSmall,
               modifier = Modifier.fillMaxWidth()
           )
       }
        SpacerUi(modifier = Modifier.height(20.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            if (selectedGovId.value != -1 && selectedCityId.value != -1) {
                LogMe.i("Gov Id ${selectedGovId.value}", "selected")
                LogMe.i("City Id ${selectedGovId.value}", "selected")
                onClicked(selectedGovId.value, selectedCityId.value)
            } else {
                isLocationError.value = true
            }
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NameAndEmailForm(
    name: String,
    onClicked: (String) -> Unit
) {
    LogMe.i("userName", name)
    val fullName = remember {
        mutableStateOf(name)
    }

    /* val email = remember {
         mutableStateOf(signUpData.email)
     }*/

    val isNameError = remember {
        mutableStateOf(false)
    }
    /*val isEmailError = remember {
        mutableStateOf(false)
    }*/
    val (focusRequester) = FocusRequester.createRefs()

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.full_name),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldName(stringResource(id = R.string.example_name), fullName, isNameError)

        /*SpacerUi(modifier = Modifier.height(8.dp))

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_email),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldEmail(email, isEmailError)*/

        SpacerUi(modifier = Modifier.height(8.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            // first validate data
            validateNameForm(
                name = fullName.value,
                isNameError
            ) {
                // fire button click
                onClicked(fullName.value)
            }
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PhoneForm(mPhone: String, onNextPressed: (String) -> Unit) {
    val phone = remember {
        mutableStateOf(mPhone)
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
            // first validate data
            validatePhoneForm(
                phone = phone.value,
                isPhoneError = isPhoneError,
                onSuccessValidation = { phone ->
                    // here trying to remove the zero number
                    //val phoneWithoutZero = phone.drop(1)
                    //LogMe.i("Registre_screen", phoneWithoutZero.toString())
                    // fire button click
                    onNextPressed(phone)
                }
            )
        })
    }
}

@Composable
private fun HeadItem(
    activeStep: StepCount,
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
                    StepItem(index, stepsList.get(index), activeStep)
                }
            }

        }
        SpacerUi(modifier = Modifier.height(8.dp))
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