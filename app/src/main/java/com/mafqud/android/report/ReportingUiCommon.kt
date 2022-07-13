package com.mafqud.android.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.TextFieldPhone
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.RowUi
import com.mafqud.android.ui.theme.TextUi

enum class StateType {
    LOST,
    FOUND
}

@Composable
fun UploadingPhotosInfo(stateType: StateType = StateType.LOST) {
    val secondString = when(stateType) {
        StateType.LOST -> R.string.upload_inst2
        StateType.FOUND -> R.string.upload_inst22
    }
    ColumnUi(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DotText(stringResource(id = R.string.upload_inst1))
        DotText(stringResource(id = R.string.upload_inst3))
        DotText(stringResource(id = secondString))

    }
}

@Composable
fun DotText(text: String) {
    RowUi(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        CircleDot()
        TextUi(
            text = text,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun CircleDot(color: Color = MaterialTheme.colorScheme.error) {
    BoxUi(
        modifier = Modifier
            .size(10.dp)
            .clip(
                RoundedCornerShape(50)
            )
            .background(color)

    ) {

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhoneReportForm(phone: MutableState<String>) {
    val isPhoneError = remember {
        mutableStateOf(false)
    }
    val (focusRequester) = FocusRequester.createRefs()

    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.phone_contact),
            style = MaterialTheme.typography.titleMedium

        )
        TextFieldPhone(phone, isPhoneError, focusRequester)
    }
}
