package com.mafqud.android.myAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.ButtonSmall
import com.mafqud.android.ui.compose.LetterIcon
import com.mafqud.android.ui.compose.TitledAppBar
import com.mafqud.android.ui.theme.*

@Composable
fun AccountScreen(onBackClicked: () -> Unit = {}) {
    ColumnUi {

        TitledAppBar(
            title = stringResource(id = R.string.my_account),
            onBackClicked = {

            }
        )
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),

            ) {
            ColumnUi(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountHead()
                SpacerUi(modifier = Modifier.height(20.dp))
                AccountBody()
                SpacerUi(modifier = Modifier.height(8.dp))
                AccountButton()

            }
        }
    }
}

@Composable
fun AccountButton() {
    ButtonAuth(title = stringResource(id = R.string.edit_info)) {

    }

}

@Composable
fun AccountBody() {
    ColumnUi(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RowUi {
            // full name
            TextUi(
                modifier = Modifier.weight(1f),
                text = "Full name",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            TextUi(
                modifier = Modifier.weight(2f),
                text = "Marwa Kamel",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }

        RowUi {

            //Email
            TextUi(
                modifier = Modifier.weight(1f),
                text = "Email",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            TextUi(
                modifier = Modifier.weight(2f),
                text = "user@gmail.com",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
        RowUi {

            //address
            TextUi(
                modifier = Modifier.weight(1f),
                text = "Address",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            TextUi(
                modifier = Modifier.weight(2f),
                text = "الدقهلية - المنصورة",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AccountHead() {
    BoxUi(
        modifier = Modifier
            .height(213.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.08f
                )
            )
            .padding(top = 8.dp, bottom = 8.dp),
    ) {
        ColumnUi(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LetterIcon(letter = "M", size = 76.dp, letterSize = MaterialTheme.typography.titleLarge)
            TextUi(
                text = "Marwa Kamel",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
            ButtonSmall(title = stringResource(id = R.string.edit)) {

            }
        }
    }

}
