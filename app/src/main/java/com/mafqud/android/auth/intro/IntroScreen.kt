package com.mafqud.android.auth.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.ImageUi
import com.mafqud.android.ui.theme.TextUi


@Composable
fun IntroScreen(onLogin: () -> Unit, onSignUp: () -> Unit) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)

    ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ImageUi(
                painter = painterResource(id = R.drawable.ic_intro_robot),
                modifier = Modifier.size(250.dp, 300.dp)
            )

            TextUi(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = stringResource(id = R.string.welcome_mafqud),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.mafqud_help),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            //login
            ButtonAuth(title = stringResource(id = R.string.login),
                textColor = MaterialTheme.colorScheme.tertiary,
                backgroundColor = MaterialTheme.colorScheme.background, onClick = {
                    onLogin()

                })

            //sign up
            ButtonAuth(title = stringResource(id = R.string.sign_up),
                textColor = MaterialTheme.colorScheme.background,
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer, onClick = {
                    onSignUp()
                })
        }

        ImageUi(
            painter = painterResource(id = R.drawable.ic_mafqud_logo),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}
