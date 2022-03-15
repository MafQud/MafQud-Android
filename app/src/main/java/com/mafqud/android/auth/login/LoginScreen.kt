package com.mafqud.android.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.IconBack
import com.mafqud.android.ui.compose.TextFieldPhone
import com.mafqud.android.ui.theme.*


@Composable
fun LoginScreen(onBackPressed: () -> Boolean) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp)

    ) {

        // back arrow
        BoxUi(
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            IconBack(onClick = {
                onBackPressed()
            })
        }

        ColumnUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val phone = remember {
                mutableStateOf("")
            }
            //image
            SpacerUi(modifier = Modifier.height(90.dp))

            ImageUi(
                painter = painterResource(id = R.drawable.ic_login),
                modifier = Modifier.size(256.dp, 240.dp)
            )

            TextUi(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.login_title),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )

            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.insert_phone_num),
                style = MaterialTheme.typography.labelLarge

            )

            TextFieldPhone(phone)
            SpacerUi(modifier = Modifier.height(52.dp))
            ButtonAuth(title = stringResource(id = R.string.next), onClick = {

            })
        }
    }
}