package com.mafqud.android.ui.android

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mafqud.android.R

import com.mafqud.android.ui.material.*
import kotlinx.coroutines.NonCancellable.cancel


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
                        buttonColor = MaterialTheme.colors.purpleAlways
                    )
                },
                dismissButton = {
                    GeneralButton(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        onClicked = {
                            isOpened.value = false

                        },
                        title = stringResource(id = R.string.cancel),
                        buttonColor = MaterialTheme.colors.whiteAlways,
                        textColor = MaterialTheme.colors.blackAlways,
                    )
                }
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
                    .background(MaterialTheme.colors.whiteAlways, shape = RoundedCornerShape(10.dp))

            ) {
                ColumnUi(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    TextUi(text = title)
                }
            }
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
