package com.mafqud.android.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.mafqud.android.R
import com.mafqud.android.util.permission.PermissionAccessType


fun Activity.openDialogForPermission(
    permissionAccessType: PermissionAccessType,
    onCancelled: () -> Unit,
) {
    val message = when (permissionAccessType) {
        PermissionAccessType.CAMERA -> getString(R.string.camera_dialog)
        PermissionAccessType.READ -> getString(R.string.storage_dialog)
        PermissionAccessType.GPS -> getString(R.string.open_gps)
    }
    //If User was asked permission before and denied
    val alertDialogBuilder: AlertDialog.Builder =
        AlertDialog.Builder(this, R.style.AlertDialogTheme)

    alertDialogBuilder.setTitle(getString(R.string.permission_needed))
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton(getString(R.string.open_setting)
    ) { dialogInterface, i ->
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts(
            "package", this.packageName,
            null
        )
        intent.data = uri
        this.startActivity(intent)
    }
    alertDialogBuilder.setNegativeButton(getString(R.string.cancel)
    ) { dialogInterface, i ->
        onCancelled()
    }
    alertDialogBuilder.setOnDismissListener {
        onCancelled()
    }

    val dialog: AlertDialog = alertDialogBuilder.create()
    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.red))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.blue))
    }
    dialog.show()
}