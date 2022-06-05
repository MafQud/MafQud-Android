package com.mafqud.android.myAccountEdit.name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonSmall
import com.mafqud.android.ui.compose.IconEdit
import com.mafqud.android.ui.compose.TextFieldName
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.other.toFirstChar

@Composable
@Preview
fun EditAccountScreen(onSaveClicked: (String) -> Unit = {}, currentName: String? = "") {
    ColumnUi {
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),

            ) {
            ColumnUi(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val fullName = remember {
                    mutableStateOf(currentName ?: "")
                }
                val char  = currentName.toFirstChar()
                AccountName(char)
                SpacerUi(modifier = Modifier.height(20.dp))
                AccountFullName(fullName)
                SpacerUi(modifier = Modifier.height(8.dp))
                AccountButton(onSaveClicked, fullName)
            }
        }
    }
}

@Composable
fun AccountButton(onSaveClicked: (String) -> Unit, fullName: MutableState<String>) {
    ButtonSmall(title = stringResource(id = R.string.save)) {
        if (fullName.value.trim().isNotEmpty()) {
            onSaveClicked(fullName.value.trim())
        }
    }

}

@Composable
fun AccountFullName(currentName: MutableState<String>) {
    ColumnUi(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val isNameError = remember {
            mutableStateOf(false)
        }
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.full_name),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldName(stringResource(id = R.string.example_name), currentName, isNameError)
    }
}

@Composable
fun AccountName(char: String) {
    BoxUi {
        RowUi {
            SpacerUi(modifier = Modifier.width(8.dp))
            BoxUi(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    ),
            ) {
                TextUi(
                    text = char,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        BoxUi(
            modifier = Modifier
                .align(Alignment.CenterStart),
        ) {
            ColumnUi {
                SpacerUi(modifier = Modifier.height(20.dp))

                IconEdit(onClick = {

                })
            }
        }
    }

}
