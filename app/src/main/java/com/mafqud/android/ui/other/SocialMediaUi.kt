package com.mafqud.android.ui.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.android.FacebookButton
import com.mafqud.android.ui.android.GoogleButton

import com.mafqud.android.ui.theme.*

@Composable
fun SocialMediaUi(
    onFacebookClicked: () -> Unit,
    onGoogleClicked: () -> Unit
) {
    ColumnUi {
        OrSignUp()
        SpacerUi(
            modifier = Modifier.height(8.dp)
        )
        RowUi(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FacebookButton(onFacebookClicked)
            GoogleButton(onGoogleClicked)
        }
    }
}

@Composable
private fun OrSignUp() {
    RowUi(verticalAlignment = Alignment.CenterVertically) {
        SpacerUi(
            modifier = Modifier
                .height(2.dp)
                .alpha(0.5f)
                .background(MaterialTheme.colors.gray200ToGray600)
                .weight(1f)

        )
        TextUi(
            modifier = Modifier.padding(4.dp),
            text = /*stringResource(id = R.string.or_sign_up)*/"",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.gray200ToGray600
        )
        SpacerUi(
            modifier = Modifier
                .height(2.dp)
                .alpha(0.5f)
                .background(MaterialTheme.colors.gray200ToGray600)
                .weight(1f)

        )
    }
}