package com.mafqud.android.ui.android

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mafqud.android.R

import com.mafqud.android.ui.material.*

@Composable
fun GoogleButton(onGoogleClicked: () -> Unit) {
    ButtonUi(
        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.white200Always),
        onClick = onGoogleClicked
    ) {
        //IconUi(painter = painterResource(id = R.drawable.ic_google))
        SpacerUi(modifier = Modifier.width(2.dp))
        TextUi(
            text = stringResource(id = R.string.google),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.blackAlways
        )
    }
}

@Composable
fun FacebookButton(onFacebookClicked: () -> Unit) {
    ButtonUi(
        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.blueAlways),
        onClick = onFacebookClicked
    ) {
        //IconUi(painter = painterResource(id = R.drawable.ic_facebook))
        SpacerUi(modifier = Modifier.width(2.dp))
        TextUi(
            text = stringResource(id = R.string.facebook),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.whiteAlways
        )
    }
}

@Composable
fun GeneralButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClicked: () -> Unit = {},
    title: String,
    cornerSize: Dp = 8.dp,
    borderColor: Color = Color.Transparent,
    buttonColor: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.whiteAlways,
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
                style = MaterialTheme.typography.body1,
                color = textColor
            )
        }


    }

}