package com.mafqud.android.ui.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*
import java.lang.ProcessBuilder.Redirect.to

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AgePickerDialog(
    startAge: Int = 1,
    endAge: Int = 100,
    startPickedAge: MutableState<Float>,
    endPickedAge: MutableState<Float>,
    isOpened: MutableState<Boolean>,
    onCancelClicked: () -> Unit = {},
    onConfirmClicked: () -> Unit = {},
) {
    ColumnUi {

        if (isOpened.value) {
            Dialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    isOpened.value = false
                },
                content = {
                    BoxUi(
                        Modifier
                            .padding(8.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(MaterialTheme.colorScheme.onPrimary)

                    ) {
                        ColumnUi(
                            Modifier
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            BoxUi(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.error)
                                    .padding(8.dp)
                            ) {
                                IconUi(
                                    imageVector = Icons.Filled.Face,
                                    modifier = Modifier.size(35.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                            TextUi(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.range_age),
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            // age data
                            val sliderPosition = remember {
                                mutableStateOf(
                                    startPickedAge.value
                                            ..endPickedAge.value
                                )
                            }
                            RowUi(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextUi(
                                    text = stringResource(id = R.string.from),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                TextUi(
                                    text = sliderPosition.value.start.toInt().toString(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            RowUi(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextUi(
                                    text = stringResource(id = R.string.to),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                TextUi(
                                    text = sliderPosition.value.endInclusive.toInt().toString(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            RowUi(
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp,
                                        start = 8.dp, end = 8.dp
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextUi(
                                    text = startAge.toString(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                TextUi(
                                    text = endAge.toString(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            RangeSlider(
                                values = sliderPosition.value, onValueChange = {
                                    /*if (it.endInclusive.toInt()
                                        - it.start.toInt() > 2) {
                                        sliderPosition.value = it
                                    }*/
                                    sliderPosition.value = it

                                },
                                valueRange = startAge.toFloat()..endAge.toFloat(),
                                //steps = 100,
                                colors = SliderDefaults.colors(
                                    activeTickColor = MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                        alpha = 0.2f
                                    ),
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                ), modifier = Modifier.padding(
                                    bottom = 8.dp,
                                    start = 8.dp, end = 8.dp
                                )
                            )

                            // next
                            TextUi(
                                modifier = Modifier
                                    .width(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        isOpened.value = false
                                        startPickedAge.value = sliderPosition.value.start
                                        endPickedAge.value = sliderPosition.value.endInclusive
                                        onConfirmClicked()
                                    }
                                    .padding(8.dp),
                                text = stringResource(id = R.string.confirm),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )

                            //cancel
                            TextUi(
                                modifier = Modifier
                                    .width(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    //.background(MaterialTheme.colorScheme.onSecondary)
                                    .clickable {
                                        isOpened.value = false
                                        onCancelClicked()
                                    }
                                    .padding(8.dp),
                                text = stringResource(id = R.string.cancel),
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                                    alpha = 0.5f
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            SpacerUi(modifier = Modifier.height(12.dp))

                        }
                    }
                }, properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            )

        }
    }

}