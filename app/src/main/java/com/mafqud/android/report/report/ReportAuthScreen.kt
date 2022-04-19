package com.mafqud.android.report.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.IconLogin
import com.mafqud.android.ui.theme.*

@Composable
@Preview
fun ReportAuthScreen(
    onReportLost: () -> Unit = {},
    onReportFound: () -> Unit = {},
    onOpenApp: () -> Unit = {},
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImageUi(
                painter = painterResource(id = R.drawable.ic_report_case),
                modifier = Modifier.size(300.dp, 185.dp)
            )

            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.mafqud_help),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            //lost
            ButtonAuth(title = stringResource(id = R.string.report_lost),
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.primary, onClick = {
                    onReportLost()

                })

            //found
            ButtonAuth(title = stringResource(id = R.string.report_found),
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer, onClick = {
                    onReportFound()
                })


            RowUi(
                modifier = Modifier
                    .clickable {
                        onOpenApp()
                    }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconLogin(iconColor = MaterialTheme.colorScheme.onPrimaryContainer)
                TextUi(
                    text = stringResource(id = R.string.open_app),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}