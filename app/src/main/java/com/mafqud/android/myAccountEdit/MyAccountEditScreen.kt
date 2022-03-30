package com.mafqud.android.myAccountEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonSmall
import com.mafqud.android.ui.compose.IconEdit
import com.mafqud.android.ui.compose.TextFieldName
import com.mafqud.android.ui.theme.*

@Composable
fun EditAccountScreen(onSaveClicked: () -> Unit) {
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
                AccountName()
                SpacerUi(modifier = Modifier.height(20.dp))
                AccountFullName()
                SpacerUi(modifier = Modifier.height(8.dp))
                AccountButton(onSaveClicked)

            }
        }
    }
}

@Composable
fun AccountButton(onSaveClicked: () -> Unit) {
    ButtonSmall(title = stringResource(id = R.string.save)) {
        onSaveClicked()
    }

}

@Composable
fun AccountFullName() {
    ColumnUi(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val fullName = remember {
            mutableStateOf("Marwa kamel")
        }
        val isNameError = remember {
            mutableStateOf(false)
        }
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.full_name),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldName(stringResource(id = R.string.example_name), fullName, isNameError)
    }
}

@Composable
fun AccountName() {
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
                    text = "M",
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
