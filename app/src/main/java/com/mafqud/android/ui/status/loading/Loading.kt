package com.mafqud.android.ui.status.loading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.redBoldToWhite

@Composable
fun CircleLoading(loadingState: Boolean) {
    if (loadingState) {
        BoxUi(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(35.dp),
                color = MaterialTheme.colors.redBoldToWhite
            )
        }
    }
}


