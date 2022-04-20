package com.mafqud.android.ui.status.loading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mafqud.android.ui.theme.BoxUi

@Composable
fun CircleLoading(loadingState: Boolean, circleSize: Dp = 35.dp) {
    if (loadingState) {
        BoxUi(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(circleSize),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


