package com.mafqud.android.results.publishCase

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
import com.mafqud.android.notification.NotificationType
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.theme.*

@Composable
@Preview
fun PublishCaseScreen(
    caseModel: CaseModel? = CaseModel(),
    onPublishClicked: () -> Unit = {}
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageUi(
                painter = painterResource(id = R.drawable.ic_report_case),
                modifier = Modifier.size(300.dp, 185.dp)
            )

            val title = when (caseModel?.caseType) {
                CaseType.MISSING -> stringResource(id = R.string.publish_found2)
                CaseType.FOUND -> stringResource(id = R.string.publish_lost2)
                else -> {
                    ""
                }
            }

            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center,
            )

            SpacerUi(modifier = Modifier.height(50.dp))

            val titleButton = when (caseModel?.caseType) {
                CaseType.MISSING -> stringResource(id = R.string.publish_found)
                CaseType.FOUND -> stringResource(id = R.string.publish_lost)
                else -> {
                    ""
                }
            }

            // publish
            ButtonAuth(title = titleButton,
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    onPublishClicked()
                })
        }

    }
}