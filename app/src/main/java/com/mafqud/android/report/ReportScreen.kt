package com.mafqud.android.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.ImageUi
import com.mafqud.android.ui.theme.SpacerUi

@Composable
@Preview
fun ReportScreen(
    onReportLost: () -> Unit = {},
    onReportFound: () -> Unit = {},
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
        ) {
            ImageUi(
                painter = painterResource(id = R.drawable.ic_report_case),
                modifier = Modifier.size(215.dp, 222.dp)
            )

            //lost
            ButtonAuth(title = stringResource(id = R.string.report_lost),
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.primary, onClick = {
                    onReportLost()

                })

            SpacerUi(modifier = Modifier.height(16.dp))

            //found
            ButtonAuth(title = stringResource(id = R.string.report_found),
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer, onClick = {
                    onReportFound()
                })
        }

        ImageUi(
            painter = painterResource(id = R.drawable.ic_mafqud_logo),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}