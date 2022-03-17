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
import com.mafqud.android.util.validation.PasswordError
import com.mafqud.android.util.validation.validateNAmeAndEmailForm
import com.mafqud.android.util.validation.validatePassAndConfirm
import com.mafqud.android.util.validation.validatePhoneForm
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
    registerFormData: MutableState<RegisterIntent.Signup>,
    activeStep: MutableState<StepCount>,
    onStepOne: (String) -> Unit,
    onStepTwo: (String) -> Unit,
    onStepThree: (String, String) -> Unit,
    onStepFour: (Int, Int) -> Unit,
    onStepFive: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ColumnUi(
            modifier = Modifier
                .fillMaxSize()
        ) {

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
                            StepCount.One -> PhoneForm(registerFormData.value.phone, onStepOne)

                            StepCount.Two -> OTPForm(onStepTwo)

                            StepCount.Three -> NameAndEmailForm(registerFormData.value, onStepThree)

                            StepCount.Four -> LocationForm(registerFormData.value, onStepFour)

                            StepCount.Five -> PassWordForm(registerFormData.value.password,onStepFive)

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OTPForm(onClicked: (String) -> Unit) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_otp),
            style = MaterialTheme.typography.titleMedium
        )

        val otpState = remember {
            mutableStateOf("")
        }
        val otpOPState = remember {
            mutableStateOf("")
        }

        OTPCommon(code = otpState.value, onFilled = {
            otpOPState.value = it
        })

        // request fake OTP
        gettingOTP { otp ->
            otpState.value = otp
        }

        SpacerUi(modifier = Modifier.height(20.dp))

        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            // TODO OTP CODE
            onClicked("")

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
private fun PassWordForm(passwordInitial: String, onClicked: (String) -> Unit) {

    val password = remember {
        mutableStateOf(passwordInitial)
    }

    val passwordConfirm = remember {
        mutableStateOf(passwordInitial)
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
private fun LocationForm(signUpData: RegisterIntent.Signup, onClicked: (Int, Int) -> Unit) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.address),
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
        SpacerUi(modifier = Modifier.height(20.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            onClicked(-1, -1)
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NameAndEmailForm(
    signUpData: RegisterIntent.Signup,
    onClicked: (String, String) -> Unit
) {
    val fullName = remember {
        mutableStateOf(signUpData.fullName)
    }

    val email = remember {
        mutableStateOf(signUpData.email)
    }

    val isNameError = remember {
        mutableStateOf(false)
    }
    val isEmailError = remember {
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
        TextFieldName(stringResource(id = R.string.example_name), fullName, isNameError)

        SpacerUi(modifier = Modifier.height(8.dp))

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.insert_email),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldEmail(email, isEmailError)

        SpacerUi(modifier = Modifier.height(8.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            // first validate data
            validateNAmeAndEmailForm(
                name = fullName.value,
                email = email.value,
                isNameError = isNameError,
                isEmailError = isEmailError,
                onSuccessValidation = { name, email ->
                    // fire button click
                    onClicked(name, email)
                }
            )
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
                    // fire button click
                    onNextPressed(phone)
                }
            )
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