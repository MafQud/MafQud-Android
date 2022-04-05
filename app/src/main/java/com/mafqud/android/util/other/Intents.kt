package com.mafqud.android.util.other

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.openGalleryForMulti(
    galleryLauncher: ActivityResultLauncher<String>
) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    galleryLauncher.launch("image/*")
}

fun FragmentActivity.openGalleryForSingle(
    galleryLauncher: ActivityResultLauncher<Intent>
) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    galleryLauncher.launch(intent)
}