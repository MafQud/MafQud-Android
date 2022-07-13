package com.mafqud.android.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.auth.openReportActivity
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBar
import com.mafqud.android.util.network.ShowNetworkErrorSnakeBarUi
import com.mafqud.android.util.network.getErrorType
import com.mafqud.android.util.validation.PasswordError
import com.mafqud.android.util.validation.validateLoginForm

data class LoginUiData(
    val phone: String,
    val password: String,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun LoginScreen(
    viewModel: LoginViewModel = LoginViewModel(LoginRepository()),
    onLoginSuccess: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    onNextPressed: (LoginUiData) -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    // ui state
    val state = viewModel.stateChannel.collectAsState()
    val stateValue = state.value


    LoadingDialog(stateValue.isLoading)

    if (stateValue.isSuccess && stateValue.data != null) {
        onLoginSuccess()
    }
    if (stateValue.networkError != null) {
        stateValue.networkError.ShowNetworkErrorSnakeBar(
            scaffoldState = scaffoldState
        )
    }
    /**
     * ui data
     */
    val phone = remember {
        mutableStateOf("")
    }
    val isPasswordError = remember { mutableStateOf(PasswordError()) }

    val isPhoneError = remember {
        mutableStateOf(false)
    }
    val pass = remember {
        mutableStateOf("")
    }
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),

        ) {

        val (focusRequester) = FocusRequester.createRefs()

        ColumnUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //image
            SpacerUi(modifier = Modifier.height(12.dp))

            ImageUi(
                painter = painterResource(id = R.drawable.ic_login),
                modifier = Modifier.size(256.dp, 240.dp)
            )

            TextUi(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.login_title),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )


            SpacerUi(modifier = Modifier.height(12.dp))

            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.insert_phone_num),
                style = MaterialTheme.typography.labelLarge

            )

            TextFieldPhone(phone, isPhoneError, focusRequester)

            SpacerUi(modifier = Modifier.height(8.dp))

            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.insert_password),
                style = MaterialTheme.typography.labelLarge

            )

            TextFieldPassword(pass, isPasswordError, focusRequester)
            SpacerUi(modifier = Modifier.height(12.dp))
            ButtonAuth(title = stringResource(id = R.string.next), onClick = {
                // first validate data
                validateLoginForm(
                    phone = phone.value,
                    password = pass.value,
                    isPhoneError = isPhoneError,
                    isPasswordError = isPasswordError,
                    onSuccessValidation = { phone, pass ->
                        // here trying to remove the zero number
                        val phoneWithoutZero = phone.drop(1)
                        // fire button click
                        onNextPressed(
                            LoginUiData(
                                phone = phoneWithoutZero,
                                password = pass,
                            )
                        )
                    }
                )

            })
        }

        // back arrow
        BoxUi(
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            IconBack(onClick = {
                onBackPressed()
            })
        }
    }
}
