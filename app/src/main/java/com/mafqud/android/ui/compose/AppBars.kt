package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.RowUi
import com.mafqud.android.ui.theme.TextUi

@Composable
fun HomeAppBar(
    userName: String = "",
    notificationCount: Int = 0,
    onNotificationClicked: () -> Unit ={}
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(
                top = 16.dp, start = 16.dp, end = 16.dp,
                bottom = 20.dp
            ), horizontalArrangement = Arrangement.SpaceBetween
    ) {

        RowUi(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            BoxUi(
                Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                TextUi(
                    text = "M",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            TextUi(
                text = "Marwa Kamel",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
        IconNotification(onClick = onNotificationClicked)

    }

}