package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.validation.PHONE_MAX_LENGTH

private val mTFHeight = 50.dp


@Composable
fun TextFieldName(placeHolderTitle: String, value: MutableState<String>) {
    TextFieldUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        value = value.value,
        onValueChange = {
            value.value = it
        },
        placeholder = {
            TextUi(
                modifier = Modifier.alpha(0.5f),
                text = placeHolderTitle,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )

        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun TextFieldEmail(value: MutableState<String>) {
    TextFieldUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        value = value.value,
        onValueChange = {
            value.value = it
        },
        placeholder = {
            TextUi(
                modifier = Modifier.alpha(0.5f),
                text = stringResource(id = R.string.example_email),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )

        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun TextFieldPhone(
    phone: MutableState<String>,
    isPhoneError: MutableState<Boolean>,
    focusRequester: FocusRequester,
    maxPhoneLength: Int = PHONE_MAX_LENGTH,
) {
    ColumnUi() {

        TextFieldUi(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            value = phone.value,
            onValueChange = {
                if (it.length <= maxPhoneLength) phone.value = it
                isPhoneError.value = false
            },
            singleLine = true,
            leadingIcon = {
                RowUi(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    TextUi(
                        modifier = Modifier.padding(4.dp),
                        text = stringResource(id = R.string.egypt_code),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge
                    )
                    SpacerUi(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .padding(vertical = 12.dp)
                            .background(MaterialTheme.colorScheme.onBackground),
                    )
                }
            },
            trailingIcon = {
                if (isPhoneError.value)
                    Icon(
                        Icons.Filled.Error,
                        stringResource(id = R.string.error_phone),
                        tint = MaterialTheme.colorScheme.error
                    )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }),
            isError = isPhoneError.value,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            )
        )

        //counter message
        TextUi(
            text = "${phone.value.length} / $maxPhoneLength",
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldPassword(
    value: MutableState<String>,
    isPasswordError: MutableState<Boolean>,
    focusRequester: FocusRequester,
) {
    val passwordVisibility = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextFieldUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .focusRequester(focusRequester),
        value = value.value,
        onValueChange = {
            value.value = it
            isPasswordError.value = false
        },
        trailingIcon = {
            if (isPasswordError.value)
                Icon(
                    Icons.Filled.Error,
                    stringResource(id = R.string.error_password),
                    tint = MaterialTheme.colorScheme.error
                )
            else {
                val image = if (passwordVisibility.value)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = {
                    passwordVisibility.value = !passwordVisibility.value
                }) {
                    IconUi(
                        imageVector = image,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        isError = isPasswordError.value,
        visualTransformation = if (passwordVisibility.value)
            VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}
