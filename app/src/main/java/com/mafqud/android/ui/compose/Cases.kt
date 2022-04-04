package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Preview
fun CaseItem(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    BoxUi(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable {

            }
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        RowUi(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            CreateUserPhoto()
            ColumnUi(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // name
                RowUi(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    ColumnUi(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        TextUi(
                            text = "Osama Ali",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleMedium
                        )

                        //address
                        RowUi(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            IconMap()
                            TextUi(
                                text = "Cairo - Egypt",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    BoxUi(
                        Modifier
                            .size(70.dp, 30.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.error)
                            .clickable {

                            },
                    ) {
                        TextUi(
                            // modifier = Modifier.padding(12.dp),
                            text = "more",
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

                // state
                TextUi(
                    text = "Lost",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )

                SpacerUi(modifier = Modifier.height(4.dp))
                //date
                TextUi(
                    text = "16/3/2022",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun CreateUserPhoto(
) {
    CardUi(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondary,
                CircleShape
            )
            .background(MaterialTheme.colorScheme.onSecondary)
            .clickable {
            }, elevation = 2.dp
    ) {
        ImageUi(
            painter = painterResource(id = R.drawable.child),
            modifier = Modifier.fillMaxSize()
        )
    }

}
