package com.mafqud.android.util.other

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mafqud.android.R
import com.mafqud.android.splash.SplashActivity


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


fun Context.showChromeCustomTabs(url: String) {

    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setToolbarColor(ContextCompat.getColor(this, R.color.red))
        .addDefaultShareMenuItem()
        .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
        .setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
        .build().also {
            it.launchUrl(this, Uri.parse(url))
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


fun Activity.inAppReview() {
    val manager = ReviewManagerFactory.create(this)

    (manager.requestReviewFlow()).addOnCompleteListener { request ->
        if (request.isSuccessful) {
            // We got the ReviewInfo object
            val reviewInfo = request.result
            manager.launchReviewFlow(this, reviewInfo)
        } else {
            //rateApp()
        }
    }

}
fun Activity.inAppUpdate() {
    // Creates instance of the manager.
    val appUpdateManager = AppUpdateManagerFactory.create(this)

    // Returns an intent object that you use to check for an update.
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

    // Checks that the platform will allow the specified type of update.
    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            // && appUpdateInfo.updatePriority() >= HIGH_PRIORITY_UPDATE
            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
        ) {
            try {
                // Request the update.
                startUpdate(appUpdateManager, appUpdateInfo)

            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().log(e.localizedMessage)
            }
        }
    }
}

private fun Activity.startUpdate(appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
    appUpdateManager.startUpdateFlowForResult(
        // Pass the intent that is returned by 'getAppUpdateInfo()'.
        appUpdateInfo,
        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
        AppUpdateType.FLEXIBLE,
        // The current activity making the update request.
        this,
        // Include a request code to later monitor this update request.
        101)
}
