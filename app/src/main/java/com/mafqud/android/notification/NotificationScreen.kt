package com.mafqud.android.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.*


@Composable
@Preview
fun NotificationScreen(onBack: () -> Unit = {}) {
    ColumnUi {
        TitledAppBar(
            title = stringResource(id = R.string.notification),
            onBackClicked = {
                onBack()
            }
        )
        BoxUi(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            BodyUi()
        }
    }


}

@Composable
private fun BodyUi() {
    ColumnUi(
        Modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
        NotificationItem()
    }
}

@Composable
fun NotificationItem() {
    BoxUi(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {

            }
            .padding(8.dp)
    ) {
        RowUi(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColumnUi(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextUi(
                    text = "تم نشر بيانات المعثور عليه بنجاح انتظر منا اشعار اخر في حين الوصول لأي نتائج",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge
                )

                //date
                TextUi(
                    text = "March,13, 2022 at 3:15 PM",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            // icon
            IconUi(
                painter = painterResource(id = R.drawable.ic_success),
                modifier = Modifier.size(24.dp)
            )

        }
    }

}

