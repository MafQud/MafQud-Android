package com.mafqud.android.util.permission

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.mafqud.android.ui.dialog.openDialogForPermission


val READ_FILES__PERMISSIONS = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE
)

val CAMERA_READ_FILES__PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)


object PermissionManager {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var onDenied: (() -> Unit)? = null
    private var onGranted: (() -> Unit)? = null
    private lateinit var permissions: Array<String>

    /**
     *  @param permissions can be a value from @LOCATION_PERMISSIONS,
     *  @see CAMERA_READ_FILES__PERMISSIONS
     *  @see READ_FILES__PERMISSIONS
     *  @see LOCATION_PERMISSIONS
     */
    fun from(
        fragment: Fragment, permissions: Array<String>,
        permissionAccessType: PermissionAccessType,
    ) {
        this.permissions = permissions
        permissionLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { mPermissions ->
                val isGranted = mPermissions.entries.all {
                    it.value
                }

                if (isGranted) onGranted?.invoke() else {
                    fragment.requireActivity().openDialogForPermission(
                        permissionAccessType,
                        onCancelled = {
                            onDenied?.invoke()
                        })

                }
            }
    }

    fun requestPermission(
        onDenied: () -> Unit,
        onGranted: () -> Unit,
    ) {
        this.onDenied = onDenied
        this.onGranted = onGranted
        permissionLauncher.launch(permissions)
    }


}