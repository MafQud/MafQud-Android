package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.RowUi
import com.mafqud.android.ui.theme.TextUi

@Composable
fun HomeAppBar(
    userName: String = "",
    notificationCount: Int = 0,
    onNotificationClicked: () -> Unit = {}
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
            LetterIcon("M", letterSize = MaterialTheme.typography.titleMedium)
            TextUi(
                text = "Marwa Kamel",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
        IconNotification(onClick = onNotificationClicked)

    }

}

@Composable
fun LetterIcon(letter: String, size: Dp = 35.dp, letterSize: TextStyle) {
    BoxUi(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        TextUi(
            text = letter,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = letterSize
        )
    }
}


@Composable
fun TitledAppBar(
    title: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onBackClicked: (() -> Unit)? = null
) {

    BoxUi(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                backgroundColor
            )
            .padding(
                top = 16.dp, start = 16.dp, end = 16.dp,
                bottom = 20.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        TextUi(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleLarge
        )

        BoxUi(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentAlignment = Alignment.CenterStart
        ) {
            if (onBackClicked != null) {
                IconBack(onClick = {
                    onBackClicked()
                })
            }
        }
    }
}

@Composable
fun CameraAppBar(
    onBackClicked: (() -> Unit)? = null,
    onDoneClicked: (() -> Unit)? = null,
) {

    BoxUi(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(
                top = 16.dp, start = 16.dp, end = 16.dp,
                bottom = 20.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        RowUi(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (onBackClicked != null) {
                IconBack(
                    onClick = {
                        onBackClicked()
                    },
                    iconColor = MaterialTheme.colorScheme.error,
                )
            }

            if (onDoneClicked != null) {
                RowUi(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onDoneClicked()
                        },
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconDone(
                        iconColor = MaterialTheme.colorScheme.primary,
                    )
                    TextUi(
                        text = stringResource(id = R.string.done),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleSmall
                    )

                }
            }
        }


    }
}