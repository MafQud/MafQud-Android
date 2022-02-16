package com.mafqud.android.util.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*


@Composable
fun NoInternet(onClickBtn: () -> Unit) {

    ColumnUi(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        IconUi(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Filled.WifiOff,
            contentDescription = null,
            tint = MaterialTheme.colors.secondaryVariant
        )

        ButtonUi( colors = ButtonDefaults.buttonColors(MaterialTheme.colors.redBoldAlways),
            onClick = onClickBtn) {
            TextUi(
                modifier = Modifier.padding(4.dp),
                text = stringResource(id = R.string.try_again),
                fontSize = 16.sp,
                color = MaterialTheme.colors.whiteAlways
            )
        }
    }

}