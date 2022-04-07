package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.IconUi

private val mIconSize = 35.dp


@Composable
@Preview
fun IconNotification(
    iconSize: Dp = mIconSize,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    alpha: Float = 0.5f,
    onClick: () -> Unit = {}
) {
    BoxUi(
        modifier = Modifier
            .size(iconSize)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor.copy(alpha = alpha))
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        BoxUi() {
            IconUi(
                imageVector = Icons.Filled.Notifications,
                tint = iconColor,
            )
            BoxUi(
                Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.error)
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {

            }
        }
    }
}

@Composable
fun IconBack(
    iconSize: Dp = mIconSize,
    iconColor: Color = MaterialTheme.colorScheme.tertiary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    alpha: Float = 1f,
    onClick: () -> Unit
) {
    BoxUi(
        modifier = Modifier
            .mirror()
            .size(iconSize)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .alpha(alpha)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        IconUi(
            imageVector = Icons.Filled.ArrowBack,
            tint = iconColor,
        )
    }
}

@Composable
fun IconMap(
    iconColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    alpha: Float = 1f,
) {
    BoxUi(
        modifier = Modifier
            .size(12.dp, 14.dp)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        IconUi(
            imageVector = Icons.Filled.FmdGood,
            tint = iconColor,
        )
    }
}

@Composable
fun IconEdit(
    iconSize: Dp = mIconSize,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    alpha: Float = 1f,
    onClick: () -> Unit
) {
    BoxUi(
        modifier = Modifier
            .mirror()
            .size(iconSize)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .alpha(alpha)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        IconUi(
            imageVector = Icons.Filled.Edit,
            tint = iconColor,
        )
    }
}


@Composable
fun IconCalender(
    iconSize: Dp = mIconSize,
    iconColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
) {
    BoxUi(
        modifier = Modifier
            .size(iconSize)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        IconUi(
            imageVector = Icons.Filled.DateRange,
            tint = iconColor,
        )
    }
}


@Composable
fun IconCamera(
    iconSize: Dp = mIconSize,
    iconColor: Color = MaterialTheme.colorScheme.tertiary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    alpha: Float = 1f,
    onClick: () -> Unit
) {
    BoxUi(
        modifier = Modifier
            .size(iconSize)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .alpha(alpha)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        IconUi(
            imageVector = Icons.Filled.AddAPhoto,
            tint = iconColor,
        )
    }
}


@Composable
fun IconGallery(
    iconSize: Dp = mIconSize,
    iconColor: Color = MaterialTheme.colorScheme.tertiary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    alpha: Float = 1f,
    onClick: () -> Unit
) {
    BoxUi(
        modifier = Modifier
            .size(iconSize)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .alpha(alpha)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        IconUi(
            imageVector = Icons.Filled.Collections,
            tint = iconColor,
        )
    }
}

