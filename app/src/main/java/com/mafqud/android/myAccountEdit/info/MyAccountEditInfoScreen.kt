package com.mafqud.android.myAccountEdit.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.theme.*

@Composable
fun MyAccountInfo() {

    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 16.dp, end = 16.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SpacerUi(modifier = Modifier.height(16.dp))
            NameAndEmailForm()
            LocationForm()
            PhoneForm()

            ButtonAuth(title = stringResource(id = R.string.save_data), onClick = {
                // first validate data

            })
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PhoneForm() {
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
            text = stringResource(id = R.string.phone_number),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldPhone(phone, isPhoneError, focusRequester)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NameAndEmailForm(

) {
    val fullName = remember {
        mutableStateOf("")
    }

    val email = remember {
        mutableStateOf("")
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

    }
}

@Composable
private fun LocationForm() {
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
                selectedItemTitle = selectedItem,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            DropDownItems(
                items = listOf("City"),
                selectedItemTitle = selectedItem,
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