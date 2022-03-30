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
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.validation.PHONE_MAX_LENGTH
import com.mafqud.android.util.validation.PassErrorType
import com.mafqud.android.util.validation.PasswordError

private val mTFHeight = 50.dp


@Composable
fun TextFieldName(
    placeHolderTitle: String,
    value: MutableState<String>,
    isNameError: MutableState<Boolean>
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ColumnUi {
            TextFieldUi(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = value.value,
                onValueChange = {
                    value.value = it
                    isNameError.value = false
                },
                placeholder = {
                    TextUi(
                        modifier = Modifier.alpha(0.5f),
                        text = placeHolderTitle,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall
                    )

                },
                trailingIcon = {
                    if (isNameError.value)
                        Icon(
                            Icons.Filled.Error,
                            stringResource(id = R.string.error_name),
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                isError = isNameError.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = Color.Transparent,
                    errorLeadingIconColor = MaterialTheme.colorScheme.error
                )
            )

            if (isNameError.value) {
                //error message
                TextUi(
                    text = stringResource(id = R.string.error_name),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun TextFieldEmail(value: MutableState<String>, isEmailError: MutableState<Boolean>) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        ColumnUi {
            TextFieldUi(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = value.value,
                onValueChange = {
                    value.value = it
                    isEmailError.value = false
                },
                placeholder = {
                    TextUi(
                        modifier = Modifier.alpha(0.5f),
                        text = stringResource(id = R.string.example_email),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall
                    )

                },
                trailingIcon = {
                    if (isEmailError.value)
                        Icon(
                            Icons.Filled.Error,
                            stringResource(id = R.string.error_name),
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                isError = isEmailError.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = Color.Transparent,
                    errorLeadingIconColor = MaterialTheme.colorScheme.error

                )
            )

            if (isEmailError.value) {
                //error message
                TextUi(
                    text = stringResource(id = R.string.error_email),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun TextFieldPhone(
    phone: MutableState<String>,
    isPhoneError: MutableState<Boolean>,
    focusRequester: FocusRequester,
    maxPhoneLength: Int = PHONE_MAX_LENGTH,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

        ColumnUi {

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
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = Color.Transparent,
                    errorLeadingIconColor = MaterialTheme.colorScheme.error
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

            if (isPhoneError.value) {
                //error message
                TextUi(
                    text = stringResource(id = R.string.error_phone),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldPassword(
    value: MutableState<String>,
    isPasswordError: MutableState<PasswordError>,
    focusRequester: FocusRequester,
) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

        val passwordVisibility = remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current

       ColumnUi {
           TextFieldUi(
               modifier = Modifier
                   .fillMaxWidth()
                   .height(50.dp)
                   .focusRequester(focusRequester),
               value = value.value,
               onValueChange = {
                   value.value = it
                   isPasswordError.value.isError = false
               },
               trailingIcon = {
                   if (isPasswordError.value.isError)
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
               isError = isPasswordError.value.isError,
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
                   focusedIndicatorColor = Color.Transparent,
                   errorCursorColor = MaterialTheme.colorScheme.error,
                   errorIndicatorColor = Color.Transparent,
                   errorLeadingIconColor = MaterialTheme.colorScheme.error
               )
           )
           if (isPasswordError.value.isError) {
               //error message
               val message = when(isPasswordError.value.type) {
                   PassErrorType.INCOMPATIBLE -> stringResource(id = R.string.error_pass_not_compat)
                   PassErrorType.LESS -> stringResource(id = R.string.error_pass_less)
                   PassErrorType.NONE -> stringResource(id = R.string.error_password)
               }
               TextUi(
                   text = message,
                   textAlign = TextAlign.Start,
                   color = MaterialTheme.colorScheme.error,
                   style = MaterialTheme.typography.titleSmall,
                   modifier = Modifier.fillMaxWidth()
               )
           }
       }
    }
}
