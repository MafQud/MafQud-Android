package com.mafqud.android.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.RowUi
import com.mafqud.android.ui.theme.TextUi
import com.mafqud.android.ui.theme.gray200ToGray600
import com.mafqud.android.ui.theme.purpleAlways

@Composable
fun TextDoubleString(grayString: String, activeString: String, onActiveClicked: () -> Unit) {
    RowUi(verticalAlignment = Alignment.CenterVertically) {
        TextUi(
            text = grayString,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.gray200ToGray600
        )

        TextUi(
            modifier = Modifier
                .padding(4.dp)
                .clickable {
                    onActiveClicked()
                },
            text = activeString,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.purpleAlways,
        )
    }
}