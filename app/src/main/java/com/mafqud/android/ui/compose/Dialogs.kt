package com.mafqud.android.ui.compose

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*


@Composable
fun LogoutDialog(isOpened: MutableState<Boolean>, onConfirmClicked: () -> Unit) {
    ColumnUi {

        if (isOpened.value) {
            AlertDialog(
                shape = RoundedCornerShape(4.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    isOpened.value = false
                },
                title = {
                    TextUi(text = stringResource(id = R.string.log_out))
                },
                text = {
                    TextUi(stringResource(id = R.string.are_you_sure_logout))
                },
                confirmButton = {
                    GeneralButton(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        onClicked = {
                            isOpened.value = false
                            onConfirmClicked()
                        }, title = stringResource(id = R.string.confirm),
                        buttonColor = MaterialTheme.colorScheme.primary
                    )
                },
                dismissButton = {
                    GeneralButton(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        onClicked = {
                            isOpened.value = false

                        },
                        title = stringResource(id = R.string.cancel),
                        buttonColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                    )
                }
            )

        }
    }

}

@Composable
fun PhotoDialog(
    imageUri: Uri,
    isOpened: MutableState<Boolean>,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
    height: Dp,
    width: Dp,
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
                    BoxUi {
                        ImageUi(
                            painter = rememberImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(width, height)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        RowUi(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            TextUi(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.onSecondary)
                                    .clickable {
                                        isOpened.value = false
                                        onConfirmClicked()
                                    }
                                    .padding(8.dp),
                                text = stringResource(id = R.string.confirm),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextUi(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.onSecondary)
                                    .clickable {
                                        isOpened.value = false
                                        onCancelClicked()
                                    }
                                    .padding(8.dp),
                                text = stringResource(id = R.string.cancel),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.titleMedium
                            )

                        }
                    }
                }, properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )

        }
    }

}

@Composable
fun WarningDialog(
    isOpened: MutableState<Boolean>,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
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
                                    painter = painterResource(id = R.drawable.ic_warning),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }

                            TextUi(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.warn_dailog_title1),
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )

                            TextUi(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.warn_dailog_title2),
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )

                            // next
                            TextUi(
                                modifier = Modifier
                                    .width(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        isOpened.value = false
                                        onConfirmClicked()
                                    }
                                    .padding(8.dp),
                                text = stringResource(id = R.string.follow),
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
                                text = stringResource(id = R.string.exit),
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
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )

        }
    }

}

@Composable
fun LoadingDialog(
    isOpened: Boolean,
    title: String = stringResource(id = R.string.please_wait)
) {
    if (isOpened) {
        Dialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                //isOpened.value = false
            },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            BoxUi(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    //.clip(RoundedCornerShape(8.dp))
                    .size(200.dp, 150.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(10.dp)
                    )

            ) {
                ColumnUi(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color =
                        MaterialTheme.colorScheme.primary
                    )
                    TextUi(
                        text = title, color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }

    }

}


fun Context.logoutDialog(onConfirmClicked: () -> Unit): AlertDialog {
    val alertDialog = MaterialAlertDialogBuilder(
        this,
        R.style.MaterialAlertDialog_rounded
    )

    // SettingsActivity Dialog Title
    alertDialog.setTitle(getString(R.string.are_you_sure_logout))

    // SettingsActivity Dialog Message
    alertDialog.setCancelable(true)
    alertDialog.setPositiveButton(
        getString(R.string.log_out)
    ) { p0, p1 ->
        onConfirmClicked()
    }

    alertDialog.setNegativeButton(
        getString(R.string.cancel)
    ) { p0, p1 ->
        p0.dismiss()
    }

    val dialog = alertDialog.create()
    //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    //2. now setup to change color of the button
    dialog.setOnShowListener {
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.black))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.red))
    }
    // Showing Alert Message
    return dialog
}

fun Context.showAreYouSureDialog(onCloseClicked: () -> Unit) {
    val alertDialog = MaterialAlertDialogBuilder(
        this,
        R.style.MaterialAlertDialog_rounded
    )

    // SettingsActivity Dialog Title
    alertDialog.setTitle(getString(R.string.are_you_sure_close))

    // SettingsActivity Dialog Message
    alertDialog.setCancelable(true)
    alertDialog.setPositiveButton(
        getString(R.string.close)
    ) { p0, p1 ->
        onCloseClicked()
    }

    alertDialog.setNegativeButton(
        getString(R.string.cancel)
    ) { p0, p1 ->
        p0.dismiss()
    }

    val dialog = alertDialog.create()
    //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    //2. now setup to change color of the button
    dialog.setOnShowListener {
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.black))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.red))
    }
    // Showing Alert Message
    dialog.show()
}

@Composable
fun AreYouSureDialog(isOpened: MutableState<Boolean>, onConfirmClicked: () -> Unit) {
    ColumnUi {

        if (isOpened.value) {
            AlertDialog(
                shape = RoundedCornerShape(4.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    isOpened.value = false
                },
                title = {
                    TextUi(text = stringResource(id = R.string.log_out))
                },
                text = {
                    TextUi(stringResource(id = R.string.are_you_sure_close))
                },
                confirmButton = {
                    GeneralButton(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        onClicked = {
                            isOpened.value = false
                            onConfirmClicked()
                        }, title = stringResource(id = R.string.close),
                        buttonColor = MaterialTheme.colorScheme.primary
                    )
                },
                dismissButton = {
                    GeneralButton(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        onClicked = {
                            isOpened.value = false

                        },
                        title = stringResource(id = R.string.cancel),
                        buttonColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                    )
                }
            )

        }
    }

}

/*@Composable
fun SuccessDialog(
    isOpened: Boolean,
    title: String = stringResource(id = R.string.password_success),
    onClosed: () -> Unit
) {
    if (isOpened) {
        Dialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                //isOpened.value = false
                onClosed()
            },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            BoxUi(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    //.clip(RoundedCornerShape(8.dp))
                    .size(200.dp)
                    .background(MaterialTheme.colors.whiteAlways, shape = RoundedCornerShape(10.dp))

            ) {
                ColumnUi(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconUi(
                        modifier = Modifier.size(80.dp),
                        imageVector = Icons.Filled.CheckCircle,
                        tint = MaterialTheme.colors.purpleAlways
                    )
                    TextUi(
                        text = title, textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.blackAlways,
                        style = MaterialTheme.typography.body1
                    )
                    TextUi(
                        text = stringResource(id = R.string.click_to_close),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.gray800All,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }

    }

}*/


/*fun Context.showAlert(message: String) {
    val alertDialog = android.app.AlertDialog.Builder(this)

    // SettingsActivity Dialog Title
    alertDialog.setTitle(getString(R.string.info))

    // SettingsActivity Dialog Message
    alertDialog.setMessage(message)
    alertDialog.setCancelable(true)

    alertDialog.create()
    // Showing Alert Message
    alertDialog.show()
}

fun Context.showAreYouSureDialog(onCloseClicked: () -> Unit) {
    val alertDialog = android.app.AlertDialog.Builder(this)

    // SettingsActivity Dialog Title
    alertDialog.setTitle(getString(R.string.are_you_sure_close))

    // SettingsActivity Dialog Message
    alertDialog.setCancelable(true)
    alertDialog.setPositiveButton(
        getString(R.string.close)
    ) { p0, p1 ->
        onCloseClicked()
    }

    alertDialog.setNegativeButton(
        getString(R.string.cancel)
    ) { p0, p1 ->
        p0.dismiss()
    }

    val dialog = alertDialog.create()
    //2. now setup to change color of the button
    dialog.setOnShowListener {
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.black))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.purple))
    }
    // Showing Alert Message
    dialog.show()
}*/
