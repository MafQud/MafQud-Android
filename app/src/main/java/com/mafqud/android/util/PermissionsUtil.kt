package com.mafqud.android.util

import android.Manifest
import androidx.activity.result.ActivityResultLauncher


val CAMERA_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

fun requestRequiredPermissions(
    requestMultiplePermissions: ActivityResultLauncher<Array<String>>,
    permissions: Array<String>
) {
    requestMultiplePermissions.launch(
        permissions
    )
}