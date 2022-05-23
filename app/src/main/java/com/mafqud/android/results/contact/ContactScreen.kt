package com.mafqud.android.results.contact

import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.name
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.myAccount.AccountBody
import com.mafqud.android.myAccount.AccountButton
import com.mafqud.android.myAccount.AccountHead
import com.mafqud.android.notification.NotificationType
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.CaseItem
import com.mafqud.android.ui.compose.IconGeneral
import com.mafqud.android.ui.compose.UserPhoto
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.animations.WavesAnimation

@Composable
@Preview
fun ContactScreen(
    onContactSuccess: () -> Unit = {},
    onContactFailed: () -> Unit = {},
    openDialer: (String) -> Unit = {},
) {
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
            SpacerUi(modifier = Modifier.height(50.dp))
            TableContent(openDialer)
        }
        BoxUi(
            Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
        ) {
            ColumnUi(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ButtonAuth(
                    title = stringResource(id = R.string.contact_success),
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    onClick = onContactSuccess
                )
                ButtonAuth(
                    title = stringResource(id = R.string.contact_failed),
                    backgroundColor = MaterialTheme.colorScheme.error,
                    onClick = onContactFailed

                )
            }
        }
    }
}

@Composable
fun TableContent(openDialer: (String) -> Unit) {
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
                // name
                TextUi(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.name),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                TextUi(
                    modifier = Modifier.weight(2f),
                    text = "مروة كامل",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            SpacerSmallLine()

            RowUi() {
                //phone
                TextUi(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.contact_phone),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                TextUi(
                    modifier = Modifier.weight(2f),
                    text = "0123456789",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                CompositionLocalProvider(
                    LocalRippleTheme provides ClearRippleTheme
                ) {
                    BoxUi(
                        Modifier
                            .size(50.dp)
                            .clickable {
                                openDialer("0123456789")
                            }) {
                        WavesAnimation(Modifier.size(50.dp))

                    }
                }

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
                    text = "الدقهلية - المنصورة شارع جيهان",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}