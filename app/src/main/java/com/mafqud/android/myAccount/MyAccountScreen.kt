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
                AccountHead(onEditClicked)
                SpacerUi(modifier = Modifier.height(20.dp))
                AccountBody()
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
fun AccountBody() {
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
                text = "Marwa Kamel",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }

        RowUi {

            //Email
            TextUi(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.email),
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
                text = stringResource(id = R.string.address),
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
fun AccountHead(onEditClicked: () -> Unit) {
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
                onEditClicked()
            }
        }
    }

}
