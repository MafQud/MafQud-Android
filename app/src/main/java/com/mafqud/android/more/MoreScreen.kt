package com.mafqud.android.more

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.logoutDialog
import com.mafqud.android.ui.theme.*

@Composable
fun MoreScreen(
    onReportedClicked: () -> Unit,
    onAccountClicked: () -> Unit,
    onSettingClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    onPhonesClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
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

            MoreItem(
                title = stringResource(id = R.string.reported_people),
                icon = R.drawable.ic_report,
                onItemClicked = onReportedClicked,
            )
            HorizontalLine()

            MoreItem(
                title = stringResource(id = R.string.my_account),
                icon = R.drawable.ic_account,
                onItemClicked = onAccountClicked,

                )
            HorizontalLine()

            MoreItem(
                title = stringResource(id = R.string.settings),
                icon = R.drawable.ic_setting,
                onItemClicked = onSettingClicked,

                )
            HorizontalLine()

            MoreItem(
                title = stringResource(id = R.string.help),
                icon = R.drawable.ic_info,
                onItemClicked = onHelpClicked,

                )
            HorizontalLine()

            MoreItem(
                title = stringResource(id = R.string.phones),
                icon = R.drawable.ic_phone,
                onItemClicked = onPhonesClicked,
            )
            HorizontalLine()
            Logout(onConfirmClicked = onLogoutClicked)

        }
    }
}

@Composable
private fun Logout(onConfirmClicked: () -> Unit) {
    val context = LocalContext.current
    val dialog = context.logoutDialog {
        onConfirmClicked()
    }
    TextUi(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                dialog.show()
            }
            .padding(8.dp),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.log_out),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
@Preview
private fun MoreItem(
    title: String = "Report",
    @DrawableRes icon: Int = R.drawable.ic_report,
    onItemClicked: () -> Unit = {}
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeftIcon(icon)
        TextUi(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )

    }
}

@Composable
fun LeftIcon(
    @DrawableRes icon: Int,
) {
    IconUi(painter = painterResource(id = icon), tint = MaterialTheme.colorScheme.primary)

}

@Composable
fun HorizontalLine() {
    SpacerUi(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(2.dp)
            .alpha(0.05f)
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
    )
}
