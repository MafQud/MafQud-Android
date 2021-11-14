package com.mafqud.android.ui.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mafqud.android.ui.material.RowUi
import com.mafqud.android.ui.material.TextUi
import com.mafqud.android.ui.material.gray200ToGray600
import com.mafqud.android.ui.material.purpleAlways

@Composable
fun DoubleString(grayString: String, activeString: String, onActiveClicked: () -> Unit) {
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