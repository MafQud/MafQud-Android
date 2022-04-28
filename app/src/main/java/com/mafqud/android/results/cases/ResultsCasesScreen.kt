package com.mafqud.android.results.cases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.notification.NotificationType
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.CaseItem
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.SpacerUi

@Composable
@Preview
fun ResultsCasesScreen(
    notificationType: NotificationType = NotificationType.NONE,
    onNotFoundButton: () -> Unit = {},
    onCaseClicked: () -> Unit = {},
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
            .padding(horizontal = 16.dp)
    ) {
        ColumnUi(
            Modifier
                .background(
                    MaterialTheme.colorScheme.onPrimary
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BodyUi(Modifier.weight(1f), onCaseClicked)
        }

        val bottomTitle = when (notificationType) {
            NotificationType.SUCCESS_FINDING_LOST ->
                stringResource(id = R.string.title_losts_not_found)
            NotificationType.SUCCESS_FINDING_FOUND ->
                stringResource(id = R.string.title_founds_not_found)
            else -> {
                ""
            }
        }

        BoxUi(
            Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
        ) {
            ButtonAuth(
                title = bottomTitle, backgroundColor = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.onPrimary
            ) {
                onNotFoundButton()
            }

        }
    }

}

@Composable
private fun BodyUi(
    modifier: Modifier,
    onCaseClicked: () -> Unit
) {
    ColumnUi(
        modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.onSecondary),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        CaseItem(onCaseClicked = onCaseClicked)
        SpacerUi(modifier = Modifier.height(100.dp))
    }
}