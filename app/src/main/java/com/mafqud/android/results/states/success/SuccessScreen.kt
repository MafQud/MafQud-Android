package com.mafqud.android.results.states.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.mapper.CaseContact
import com.mafqud.android.notification.CaseModel
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.TextFieldNationalID
import com.mafqud.android.ui.compose.WarningDialog
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.validation.validateLoginForm
import com.mafqud.android.util.validation.validateNationalIDForm

@Composable
@Preview
fun SuccessScreen(
    caseContact: CaseContact? = CaseContact(),
    nationalID: String = "",
    showResults: (String) -> Unit = {}
) {
    val successDialog = remember {
        mutableStateOf(false)
    }
    val nationalIdState = remember {
        mutableStateOf(nationalID)
    }
    val isIdError = remember {
        mutableStateOf(false)
    }
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

            ImageUi(
                painter = painterResource(id = R.drawable.ic_success),
                modifier = Modifier.size(286.dp, 252.dp)
            )

            val firstString = when (caseContact?.caseType) {
                CaseType.MISSING -> stringResource(id = R.string.success_results_lost)
                CaseType.FOUND -> stringResource(id = R.string.success_results_found)
                CaseType.NONE -> stringResource(id = R.string.error_unknown)
                else -> {
                    stringResource(id = R.string.error_unknown)
                }
            }
            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = firstString,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )


            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.insert_id),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center,
            )

            NationalIdField(nationalIdState, isIdError)

            ButtonAuth(title = stringResource(id = R.string.follow)) {
                validateNationalIDForm(
                    id = nationalIdState.value,
                    isIdError = isIdError
                ) {
                    successDialog.value = true
                }
            }

            SpacerUi(modifier = Modifier.height(20.dp))

        }
        val dialogTitle = when (caseContact?.caseType) {
            CaseType.MISSING -> stringResource(id = R.string.warn_dailog_title_lost)
            CaseType.FOUND -> stringResource(id = R.string.warn_dailog_title_found)
            CaseType.NONE -> stringResource(id = R.string.error_unknown)
            else -> {
                stringResource(id = R.string.error_unknown)
            }
        }
        WarningDialog(
            titleHead = dialogTitle,
            isOpened = successDialog,
            onConfirmClicked = {
                showResults(nationalIdState.value)
            },
            onCancelClicked = {

            }
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NationalIdField(nationalId: MutableState<String>, isIdError: MutableState<Boolean>) {

    TextFieldNationalID(nationalId, isIdError)

}
