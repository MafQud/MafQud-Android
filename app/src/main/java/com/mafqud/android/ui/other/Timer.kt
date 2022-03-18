package com.mafqud.android.ui.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.RowUi
import com.mafqud.android.ui.theme.TextUi
import com.mafqud.android.util.other.getTimeFromSec
import kotlinx.coroutines.delay


@Composable
fun TimerUi(
    isTimerRunning: MutableState<Boolean>,
    currentTime: MutableState<Long>,
) {
    val timeDisplayed = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = null) {
        while (currentTime.value > 0 && isTimerRunning.value) {
            currentTime.value = currentTime.value - 1L
            timeDisplayed.value = getTimeFromSec(currentTime.value)
            delay(1000L)
        }
    }

    RowUi(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        TextUi(
            text = stringResource(id = R.string.code_time),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )

        TextUi(
            text = timeDisplayed.value,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleSmall
        )

        TextUi(
            text = stringResource(id = R.string.secound),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )
    }
}
