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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.FragmentActivity
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePickerDialog(
    startPickedDate: MutableState<String>,
    endPickedDate: MutableState<String>,
    isOpened: MutableState<Boolean>,
    onCancelClicked: () -> Unit = {},
    onConfirmClicked: () -> Unit = {},
    activity: FragmentActivity
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
                                TextUi(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    text = stringResource(id = R.string.range_date),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            // date -> date
                            val startFormattedDate = remember {
                                mutableStateOf(startPickedDate.value)
                            }

                            val startDisplayedDate = remember {
                                mutableStateOf("")
                            }

                            val startPicker = datePicker(updatedDate = { global, display ->
                                startFormattedDate.value = global
                                startDisplayedDate.value = display
                            })
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
                                // start date picker
                                TextUi(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.5f
                                            )
                                        )
                                        .clickable {
                                            startPicker.show(
                                                activity.supportFragmentManager,
                                                "start"
                                            )
                                        }
                                        .padding(8.dp),
                                    text = startDisplayedDate.value.ifEmpty {
                                        stringResource(
                                            id = R.string.choose
                                        )
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            val endFormattedDate = remember {
                                mutableStateOf(startPickedDate.value)
                            }
                            val endDisplayedDate = remember {
                                mutableStateOf("")
                            }
                            val endPicker = datePicker(updatedDate = { global, display ->
                                endFormattedDate.value = global
                                endDisplayedDate.value = display
                            })
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
                                // end date picker
                                TextUi(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.5f
                                            )
                                        )
                                        .clickable {
                                            endPicker.show(activity.supportFragmentManager, "end")
                                        }
                                        .padding(8.dp),
                                    text = endDisplayedDate.value.ifEmpty {
                                        stringResource(
                                            id = R.string.choose
                                        )
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }


                            // next
                            TextUi(
                                modifier = Modifier
                                    .width(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        isOpened.value = false
                                        startPickedDate.value = startFormattedDate.value
                                        endPickedDate.value = endFormattedDate.value
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