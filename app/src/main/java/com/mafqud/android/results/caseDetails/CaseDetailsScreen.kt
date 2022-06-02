package com.mafqud.android.results.caseDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.results.caseDetails.models.CaseDetailsResponse
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.UserPhoto
import com.mafqud.android.ui.theme.*

@Composable
@Preview
fun CaseDetailsScreen(
    caseDetailsResponse: CaseDetailsResponse? =null,
    onContact: () -> Unit = {}
) {
    caseDetailsResponse?.let { case ->
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.onPrimary
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            ColumnUi(
                Modifier
                    .background(
                        MaterialTheme.colorScheme.onPrimary
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpacerUi(modifier = Modifier.height(20.dp))
                UserPhoto(imagesSize = 80.dp)
                TextUi(
                    text = case.details?.name ?: stringResource(id = R.string.no_name),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                SpacerSmallLine()
                LazyRowUi(content = {
                    item {
                        UserPhoto(imagesSize = 47.dp)
                    }
                    item {
                        UserPhoto(imagesSize = 47.dp)
                    }
                })
                SpacerUi(modifier = Modifier.height(20.dp))
                TableContent(case)

            }
            BoxUi(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
            ) {
                ButtonAuth(
                    title = stringResource(id = R.string.contact),
                    backgroundColor = MaterialTheme.colorScheme.green, onClick = onContact,
                    icon = Icons.Filled.Call
                )
            }
        }
    }
}

@Composable
fun TableContent(case: CaseDetailsResponse) {
    BoxUi(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp)
    ) {

        ColumnUi(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RowUi {
                // date
                TextUi(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.data_founded),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                TextUi(
                    modifier = Modifier.weight(2f),
                    text = case.details?.lastSeen ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            SpacerSmallLine()

            RowUi {

                //Email
                TextUi(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.age),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                TextUi(
                    modifier = Modifier.weight(2f),
                    text = case.details?.age?.toString() ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            SpacerSmallLine()

            RowUi {

                //address
                TextUi(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.address_founded),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                TextUi(
                    modifier = Modifier.weight(2f),
                    text = case.location?.getFullAddress() ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
