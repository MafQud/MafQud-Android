package com.mafqud.android.report.lost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.*
import com.mafqud.android.ui.theme.*


@Composable
@Preview
fun LostScreenTwo() {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SpacerUi(modifier = Modifier.height(16.dp))
            HeaderTwo()
            LostName()
            LostGenderAndAge()
            LostDate()
            LostDescription()
            LocationForm()

            ButtonAuth(
                enabled = true,
                title = stringResource(id = R.string.search_losts),
                onClick = {

                })

            SpacerUi(modifier = Modifier.height(16.dp))

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
            color = MaterialTheme.colorScheme.secondaryContainer
        )

    }
}

@Composable
fun LostDate() {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // full name
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_date),
            style = MaterialTheme.typography.titleMedium

        )
        DateUi()
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.lost_date_title),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}


@Composable
private fun LostGenderAndAge() {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.gender_age),
            style = MaterialTheme.typography.titleMedium

        )
        RowUi(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DropDownItems(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                items = listOf("Gender"),
                selectedItemID = selectedItem,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            DropDownItems(
                items = listOf("Age"),
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
