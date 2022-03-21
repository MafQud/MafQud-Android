package com.mafqud.android.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ButtonUi
import com.mafqud.android.ui.theme.TextUi

private val mButtonHeight = 50.dp


@Composable
fun ButtonAuth(
    title: String, height: Dp = mButtonHeight,
    textColor: Color = MaterialTheme.colorScheme.background,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BoxUi {
        ButtonUi(
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(50)),
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor
            )
        ) {
            TextUi(
                text = title,
                color = textColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Composable
fun ButtonSmall(
    title: String, height: Dp = mButtonHeight,
    textColor: Color = MaterialTheme.colorScheme.background,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BoxUi {
        ButtonUi(
            enabled = enabled,
            modifier = Modifier
                .height(height)
                .width(130.dp)
                .clip(RoundedCornerShape(50)),
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor
            )
        ) {
            TextUi(
                text = title,
                color = textColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun GeneralButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClicked: () -> Unit = {},
    title: String,
    cornerSize: Dp = 8.dp,
    borderColor: Color = Color.Transparent,
    buttonColor: Color = Color.Black,
    textColor: Color = Color.Black,
) {
    val mModifier =
        modifier.then(
            Modifier
                .border(1.dp, borderColor, RoundedCornerShape(cornerSize))
                .clip(RoundedCornerShape(cornerSize))
        )
    ButtonUi(
        modifier = mModifier, onClick = onClicked,
        colors = ButtonDefaults.buttonColors(buttonColor)
    ) {
        BoxUi(contentAlignment = Alignment.Center) {
            TextUi(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = textColor
            )
        }


    }
}