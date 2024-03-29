package com.mafqud.android.results.states.failed

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.notification.CaseModel
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.theme.*


@Keep
enum class FailureType {
    LOST,
    FOUND,
    NONE
}

@Composable
@Preview
fun FailedScreen(caseModel: CaseModel? = CaseModel(), onPublish: () -> Unit = {}) {
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
            SpacerUi(modifier = Modifier.height(25.dp))

            RobotImage()

            // create the rest of ui fragment if not FailureType.NONE
            if (caseModel?.caseType != CaseType.NONE) {
                FirstString()

                val buttonTitle = when (caseModel?.caseType) {
                    CaseType.MISSING -> stringResource(id = R.string.publish_lost_data)
                    CaseType.FOUND -> stringResource(id = R.string.publish_found_data)
                    else -> ""
                }
                SecondString(caseModel?.caseType)

                ButtonAuth(
                    title = buttonTitle,
                    backgroundColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    onPublish()
                }
            }


        }
    }

}

@Composable
fun SecondString(caseType: CaseType?) {
    val secondString = when (caseType) {
        CaseType.MISSING -> stringResource(id = R.string.sorry_results_lost_2)
        CaseType.FOUND -> stringResource(id = R.string.sorry_results_found_2)
        CaseType.NONE -> stringResource(id = R.string.sorry_results_lost_2)
        else -> stringResource(id = R.string.error_unknown)
    }
    TextUi(
        modifier = Modifier.fillMaxWidth(),
        text = secondString,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun FirstString() {
    TextUi(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.sorry_results_lost),
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun RobotImage() {
    ImageUi(
        painter = painterResource(id = R.drawable.ic_failure_robot),
        modifier = Modifier.size(287.dp, 188.dp)
    )
}
