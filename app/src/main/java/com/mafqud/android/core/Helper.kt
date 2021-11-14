package com.mafqud.android.core

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mafqud.android.R
import com.mafqud.android.SplashActivity


fun Window.hideStatusBar() {
    this.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
}

fun Context.openDialer(number: String = "") {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.openBrowser(link: String = "") {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    //if (browserIntent.resolveActivity(packageManager) != null) {
        startActivity(browserIntent)
    //}
}

fun Context.openInstagram(link: String = "") {
    val uri = Uri.parse(link)
    val likeIng = Intent(Intent.ACTION_VIEW, uri)

    likeIng.setPackage("com.instagram.android")

    if (likeIng.resolveActivity(packageManager) != null) {
        startActivity(likeIng)
    } else {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                uri
            )
        )
    }
}

fun Context.openGooglePlay() {
    val uri: Uri = Uri.parse("market://details?id=$packageName")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    // To count with Play market backstack, After pressing back button,
    // to taken back to our application, we need to add following flags to intent.
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    if (goToMarket.resolveActivity(packageManager) != null) {
        startActivity(goToMarket)
    } else {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun String.wordCount(): Int {
    val trimmedStr = this.trim()
    return if (trimmedStr.isEmpty()) {
        0
    } else {
        trimmedStr.split("\\s+".toRegex()).size
    }
}

fun Context.changLanguage(lang: String) {
    if (lang.equals("ar", ignoreCase = true)) {
        Localization.setLanguage(this, "ar")
    } else {
        Localization.setLanguage(this, "en")
    }

}

fun Activity.restartApplication() {
    startActivity(Intent(this, SplashActivity::class.java))
    this.overridePendingTransition(0, 0);
    this.finish()
}

fun Activity.statusBarColor(@ColorInt resColor: Int) {
    val window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resColor
    } else {
        window.statusBarColor = resources.getColor(R.color.black)
    }
}

fun FragmentActivity.openGallery(
    galleryLauncher: ActivityResultLauncher<Intent>
) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    galleryLauncher.launch(Intent.createChooser(intent, getString(R.string.pic_photo)))
}

fun Context.getAppName(): String = applicationInfo.loadLabel(packageManager).toString()

