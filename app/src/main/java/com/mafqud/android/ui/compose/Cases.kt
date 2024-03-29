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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.home.model.CasesDataResponse
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.dateFormat.fromNormalDateToFull
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Preview
fun CaseItem(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    caseData: CasesDataResponse.Case? = null,
    onCaseClicked: ((CasesDataResponse.Case) -> Unit)? = {},
) {


    caseData?.let { case ->
        BoxUi(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .clickable {
                    if (onCaseClicked != null) {
                        onCaseClicked(case)
                    }
                }
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            RowUi(
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Top
            ) {
                UserPhoto(imageUrl = case.thumbnail ?: "")
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
                                text = case.name ?: stringResource(id = com.mafqud.android.R.string.no_name),
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
                                    text = case.getFullAddress(),
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
                                    if (onCaseClicked != null) {
                                        onCaseClicked(case)
                                    }
                                },
                        ) {
                            TextUi(
                                // modifier = Modifier.padding(12.dp),
                                text = stringResource(id = R.string.more),
                                color = MaterialTheme.colorScheme.onSecondary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    // state
                    val type = when(case.getCaseType()) {
                        CaseType.FOUND -> stringResource(id = R.string.found)
                        CaseType.MISSING -> stringResource(id = R.string.lost)
                        CaseType.NONE -> stringResource(id = R.string.none)
                    }
                    TextUi(
                        text = type,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )

                    SpacerUi(modifier = Modifier.height(8.dp))
                    //date
                    val date = fromNormalDateToFull(case.lastSeen)
                    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val dateTitle = when(case.getCaseType()) {
                            CaseType.FOUND -> stringResource(id = R.string.found_date)
                            CaseType.MISSING -> stringResource(id = R.string.lost_date)
                            CaseType.NONE -> ""
                        }
                        TextUi(
                            text = dateTitle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall
                        )
                        TextUi(
                            text = date.ifEmpty { stringResource(id = R.string.not_found) },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun UserPhoto(
    imageUrl: String? = "",
    imagesSize: Dp = 45.dp,
    onClicked: () -> Unit = {}
) {
    CardUi(
        modifier = Modifier
            .size(imagesSize)
            .clip(CircleShape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondary,
                CircleShape
            )
            .background(MaterialTheme.colorScheme.onSecondary)
            .clickable {
                onClicked()
            }, elevation = 2.dp
    ) {
        LoadImageAsync(
            modifier = Modifier
                .padding(2.dp)
                .clip(CircleShape),
            imageUrl
        )
    }

}


