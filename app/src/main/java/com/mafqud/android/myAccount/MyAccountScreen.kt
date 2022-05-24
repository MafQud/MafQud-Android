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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.ButtonSmall
import com.mafqud.android.ui.compose.LetterIcon
import com.mafqud.android.ui.theme.*

@Composable
@Preview
fun AccountScreen(
    onEditClicked: () -> Unit = {},
    onEditInfoClicked: () -> Unit = {},
    userName: String = "",
    address: String = "",
    phone: String = "",
) {
    ColumnUi {
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
                AccountHead(userName, onEditClicked)
                SpacerUi(modifier = Modifier.height(20.dp))
                AccountBody(userName, address, phone)
                SpacerUi(modifier = Modifier.height(8.dp))
                AccountButton(onEditInfoClicked)

            }
        }
    }
}

@Composable
fun AccountButton(onEditInfoClicked: () -> Unit) {
    ButtonAuth(title = stringResource(id = R.string.edit_info)) {
        onEditInfoClicked()
    }

}

@Composable
fun AccountBody(userName: String, address: String, phone: String) {
    ColumnUi(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RowUi {
            // full name
            TextUi(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.full_name),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            TextUi(
                modifier = Modifier.weight(2f),
                text = userName,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }

        RowUi {

            //Email
            TextUi(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.phone_number),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            TextUi(
                modifier = Modifier.weight(2f),
                text = "$phone 20+",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
        RowUi {

            //address
            TextUi(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.address),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            TextUi(
                modifier = Modifier.weight(2f),
                text = address,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AccountHead(userName: String, onEditClicked: () -> Unit) {
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
            val char = userName.firstOrNull()?.let {
                return@let it
            } ?: ""
            LetterIcon(
                letter = char.toString(),
                size = 76.dp,
                letterSize = MaterialTheme.typography.titleLarge
            )
            TextUi(
                text = userName,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
            ButtonSmall(title = stringResource(id = R.string.edit)) {
                onEditClicked()
            }
        }
    }

}
