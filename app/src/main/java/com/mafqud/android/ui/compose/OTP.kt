package com.mafqud.android.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.RowUi
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun OTPCommon(
    code: String = "",
    otpLength: Int = 4,
    /*reset: Boolean = false,*/
    displayDelay: Long = 150,
    onFilled: (String) -> Unit ={},
) {

    // if the inserted mCode is filled so, call onFilled listener
    // manual OTP
    val otpState1 = remember {
        mutableStateOf("")
    }
    val otpState2 = remember {
        mutableStateOf("")
    }
    val otpState3 = remember {
        mutableStateOf("")
    }
    val otpState4 = remember {
        mutableStateOf("")
    }
    val localOTP = otpState1.value + otpState2.value + otpState3.value + otpState4.value
    // check fill state
    /* var isValidate by remember {
         mutableStateOf(false)
     }*/
    onFilled(localOTP)
    /* isValidate = localOTP.length == otpLength
     if (isValidate) {
         onFilled(localOTP)
         Log.i("OTPCommon: ", "send")
     }*/

    // auto otp fill
    if (code.length == otpLength) {
        LaunchedEffect(key1 = Unit, block = {
            otpState1.value = code[0].toString()
            delay(displayDelay)
            otpState2.value = code[1].toString()
            delay(displayDelay)
            otpState3.value = code[2].toString()
            delay(displayDelay)
            otpState4.value = code[3].toString()
        })

    }

    val focusRequesters: List<FocusRequester> = remember {
        val temp = mutableListOf<FocusRequester>()
        repeat(otpLength) {
            temp.add(FocusRequester())
        }
        temp
    }
    RowUi(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CommonOtpTextField(otp = otpState1, focusRequesters, 0)
        CommonOtpTextField(otp = otpState2, focusRequesters, 1)
        CommonOtpTextField(otp = otpState3, focusRequesters, 2)
        CommonOtpTextField(otp = otpState4, focusRequesters, 3)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommonOtpTextField(
    otp: MutableState<String>, focusRequesters: List<FocusRequester>, index: Int
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val max = 1
    OutlinedTextField(
        value = otp.value,
        singleLine = true,
        onValueChange = { it ->
            if (it.length <= max) otp.value = it
            if (it.isNotEmpty()) {
                focusRequesters[index].freeFocus()
                if (index != focusRequesters.lastIndex) {
                    focusRequesters[index + 1].requestFocus()
                } else {
                    keyboardController?.hide()
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (index != focusRequesters.lastIndex) ImeAction.Next else ImeAction.Done,
            keyboardType = KeyboardType.Phone
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (index != focusRequesters.lastIndex)
                    focusRequesters[index + 1].requestFocus()
            },
            onDone = {
                keyboardController?.hide()
            }),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .size(64.dp)
            .focusOrder(focusRequester = focusRequesters[index]) {
                if (index != focusRequesters.lastIndex)
                    focusRequesters[index + 1].requestFocus()
            },
        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        )
    )
}