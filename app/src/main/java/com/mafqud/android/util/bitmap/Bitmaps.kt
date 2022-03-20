package com.mafqud.android.util.bitmap

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.mafqud.android.R


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


fun Context.getMarkerBitmapFromView(/*@DrawableRes resId: Int*/): Bitmap {
    val customMarkerView: View =
        (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?)!!.inflate(
            R.layout.view_custom_marker,
            null
        )
    val markerImageView = customMarkerView.findViewById<View>(R.id.iv_map_image) as ImageView
    //markerImageView.setImageResource(resId)
    customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
    customMarkerView.buildDrawingCache()
    val returnedBitmap = Bitmap.createBitmap(
        customMarkerView.measuredWidth, customMarkerView.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    val drawable = customMarkerView.background
    drawable?.draw(canvas)
    customMarkerView.draw(canvas)
    return returnedBitmap
}
fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
    // pin
    val vectorDrawable = getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    vectorDrawable.setTint(context.resources.getColor(com.mafqud.android.R.color.blue))
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)

    val centreX = 25

    val centreY = 25
    //image
    val vectorDrawable2 = getDrawable(context, com.mafqud.android.R.drawable.child)
    vectorDrawable2!!.setBounds(
        centreX,
        centreY,
        80 ,
        80
    )

    vectorDrawable.draw(canvas)
    vectorDrawable2.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}