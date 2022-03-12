package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.RowUi
import com.mafqud.android.ui.theme.SpacerUi
import com.mafqud.android.ui.theme.TextFieldUi
import com.mafqud.android.ui.theme.TextUi

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
fun TextFieldPhone(value: MutableState<String>) {
    TextFieldUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        value = value.value,
        onValueChange = {
            value.value = it
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}
