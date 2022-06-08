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
import com.mafqud.android.home.model.CasesDataResponse


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

fun Context.getMarkerBitmapFromView(case: CasesDataResponse.Case, bitmap: Bitmap): Bitmap {
    val customMarkerView: View =
        (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?)!!.inflate(
            R.layout.view_custom_marker,
            null
        )
    val markerImageView = customMarkerView.findViewById<View>(R.id.iv_map_image) as ImageView
    val caseName = customMarkerView.findViewById<View>(R.id.tv_case_name) as TextView
    caseName.text = case.name ?: getString(R.string.no_name)
    markerImageView.setImageBitmap(bitmap)
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


fun Context.downloadBitmapFromUrl(
    photoUrl: String,
    onBitmapIsReady: (Bitmap) -> Unit
) {

    Glide.with(this.applicationContext)
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
        80,
        80
    )

    vectorDrawable.draw(canvas)
    vectorDrawable2.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Bitmap.blur(scale: Float = 0.1f, radius: Int = 5): Bitmap? {
    var sentBitmap = this.toResizedBitmap()
    val width = Math.round(sentBitmap.width * scale)
    val height = Math.round(sentBitmap.height * scale)
    sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false)
    val bitmap = sentBitmap.copy(sentBitmap.config, true)
    if (radius < 1) {
        return null
    }
    val w = bitmap.width
    val h = bitmap.height
    val pix = IntArray(w * h)
    Log.e("pix", w.toString() + " " + h + " " + pix.size)
    bitmap.getPixels(pix, 0, w, 0, 0, w, h)
    val wm = w - 1
    val hm = h - 1
    val wh = w * h
    val div = radius + radius + 1
    val r = IntArray(wh)
    val g = IntArray(wh)
    val b = IntArray(wh)
    var rsum: Int
    var gsum: Int
    var bsum: Int
    var x: Int
    var y: Int
    var i: Int
    var p: Int
    var yp: Int
    var yi: Int
    var yw: Int
    val vmin = IntArray(Math.max(w, h))
    var divsum = div + 1 shr 1
    divsum *= divsum
    val dv = IntArray(256 * divsum)
    i = 0
    while (i < 256 * divsum) {
        dv[i] = i / divsum
        i++
    }
    yi = 0
    yw = yi
    val stack = Array(div) { IntArray(3) }
    var stackpointer: Int
    var stackstart: Int
    var sir: IntArray
    var rbs: Int
    val r1 = radius + 1
    var routsum: Int
    var goutsum: Int
    var boutsum: Int
    var rinsum: Int
    var ginsum: Int
    var binsum: Int
    y = 0
    while (y < h) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        i = -radius
        while (i <= radius) {
            p = pix[yi + Math.min(wm, Math.max(i, 0))]
            sir = stack[i + radius]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rbs = r1 - Math.abs(i)
            rsum += sir[0] * rbs
            gsum += sir[1] * rbs
            bsum += sir[2] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            i++
        }
        stackpointer = radius
        x = 0
        while (x < w) {
            r[yi] = dv[rsum]
            g[yi] = dv[gsum]
            b[yi] = dv[bsum]
            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum
            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]
            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]
            if (y == 0) {
                vmin[x] = Math.min(x + radius + 1, wm)
            }
            p = pix[yw + vmin[x]]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]
            rsum += rinsum
            gsum += ginsum
            bsum += binsum
            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer % div]
            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]
            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]
            yi++
            x++
        }
        yw += w
        y++
    }
    x = 0
    while (x < w) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        yp = -radius * w
        i = -radius
        while (i <= radius) {
            yi = Math.max(0, yp) + x
            sir = stack[i + radius]
            sir[0] = r[yi]
            sir[1] = g[yi]
            sir[2] = b[yi]
            rbs = r1 - Math.abs(i)
            rsum += r[yi] * rbs
            gsum += g[yi] * rbs
            bsum += b[yi] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            if (i < hm) {
                yp += w
            }
            i++
        }
        yi = x
        stackpointer = radius
        y = 0
        while (y < h) {

            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum
            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]
            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]
            if (x == 0) {
                vmin[y] = Math.min(y + r1, hm) * w
            }
            p = x + vmin[y]
            sir[0] = r[p]
            sir[1] = g[p]
            sir[2] = b[p]
            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]
            rsum += rinsum
            gsum += ginsum
            bsum += binsum
            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer]
            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]
            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]
            yi += w
            y++
        }
        x++
    }
    Log.e("pix", w.toString() + " " + h + " " + pix.size)
    bitmap.setPixels(pix, 0, w, 0, 0, w, h)
    return bitmap
}

/**
 * reduces the size of the image
 * @param image
 * @param maxSize
 * @return
 */
fun Bitmap.toResizedBitmap(maxSize: Int = 300): Bitmap {
    var width = this.width
    var height = this.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(this, width, height, true)
}