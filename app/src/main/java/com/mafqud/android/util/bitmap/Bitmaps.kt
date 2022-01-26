package com.mafqud.android.util.bitmap

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mafqud.android.R
import java.io.IOException
import java.io.InputStream


fun ImageView.toBitmap(): Bitmap {
    this.isDrawingCacheEnabled = true;
    return this.drawingCache
}

fun TextView.toBitmap(): Bitmap {
    this.isDrawingCacheEnabled = true;
    return this.drawingCache
}

fun screenShoot(view: View): Bitmap? {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun overlay(bmp1: Bitmap, bmp2: Bitmap, bmp3: Bitmap): Bitmap? {
    val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
    val canvas = Canvas(bmOverlay)
    canvas.drawBitmap(bmp1, Matrix(), null)
    canvas.drawBitmap(bmp2, 0f, 0f, null)
    canvas.drawBitmap(bmp3, 150f, 150f, null)
    bmp1.recycle()
    bmp2.recycle()
    bmp3.recycle()
    return bmOverlay
}

fun Context.downloadBitmapFromUrl(
    photoUrl: String,
    onBitmapIsReady: (Bitmap) -> Unit
) {

    Glide.with(this)
        .asBitmap()
        .load(photoUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                onBitmapIsReady(resource)
                Log.i("onResourceReady", resource.width.toString())
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // this is called when imageView is cleared on lifecycle call or for
                // some other reason.
                // if you are referencing the bitmap somewhere else too other than this imageView
                // clear it here as you can no longer have the bitmap
            }
        })

}