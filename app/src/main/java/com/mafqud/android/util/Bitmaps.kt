package com.mafqud.android.util

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
import com.google.android.libraries.maps.model.BitmapDescriptor
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
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

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    vectorDrawable.setTint(context.resources.getColor(R.color.purple))
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun getBitmapFromAsset(context: Context, strName: String): Bitmap? {
    val assetManager: AssetManager = context.getAssets()
    var istr: InputStream? = null
    return try {
        istr = assetManager.open(strName)
        val b = BitmapFactory.decodeStream(istr)
        Bitmap.createScaledBitmap(b, 200, 200, false)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

/*fun getSvgFromAsset(context: Context, strName: String): Bitmap {
    // Load and parse a SVG
    // Load and parse a SVG
    val svg = SVGBuilder()
        .readFromAsset(context.assets, strName) // if svg in assets
        // .setWhiteMode(true) // draw fills in white, doesn't draw strokes
        // .setColorSwap(0xFF008800, 0xFF33AAFF) // swap a single colour
        // .setColorFilter(filter) // run through a colour filter
        // .set[Stroke|Fill]ColorFilter(filter) // apply a colour filter to only the stroke or fill
        .build()

    // Turn into a drawable
    return svg.drawable.toBitmap()
}*/
fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
    var drawable = ContextCompat.getDrawable(context!!, drawableId)
    drawable = DrawableCompat.wrap(drawable!!).mutate()
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}