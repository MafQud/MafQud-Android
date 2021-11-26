package com.mafqud.android.util.fileManager

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import com.mafqud.android.util.other.getAppName
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

internal const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
fun Activity.getOutputAppSpecificDirectory(): File {
    return filesDir
}

fun Activity.getOutputSharedStorageDirectory(): File {
    var output: File = Environment.getDataDirectory()
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        // val publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
        val publicDir = Environment.getExternalStorageDirectory()
        output = File(publicDir, getAppName())
        if (!output.exists()) output.mkdir()

    } else {
        // @reference: https://developer.android.com/training/data-storage/app-specific
        // show a dialog first to request from user to free space up before save our pic
        // To request that the user remove all app cache files instead, set
        // "action" to ACTION_CLEAR_APP_CACHE.
        /*
        Caution: The ACTION_CLEAR_APP_CACHE intent action can substantially affect device battery life and might remove a large number of files from the device.
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            Intent(StorageManager.ACTION_MANAGE_STORAGE).apply {
                startActivity(this)
            }
        }
    }
    return output
}

fun getCurrentDate(): String {
    return SimpleDateFormat(
        FILENAME_FORMAT, Locale.US
    ).format(System.currentTimeMillis())
}

fun getCurrentDateMilliSec(): String {
    return System.currentTimeMillis().toString()
}

fun getFileWithName(directory: File, name: String = getCurrentDate()): File {
    val pictureFile: File = directory
    // Create time-stamped output file to hold the image
    return File(
        pictureFile,
        "$name.jpg"
    )
}

/*fun Activity.saveBitmapToImage(image: Bitmap) {
    val pictureDir: File = this.getOutputSharedStorageDirectory()
    // Create time-stamped output file to hold the image
    val photoFile = getFileWithName(pictureDir)
    try {
        val fos = FileOutputStream(photoFile)
        image.compress(Bitmap.CompressFormat.PNG, 90, fos)
        fos.close()
    } catch (e: FileNotFoundException) {
        Log.d("TAG", "File not found: " + e.message)
    } catch (e: IOException) {
        Log.d("TAG", "Error accessing file: " + e.message)
    }
}*/

fun Activity.getStoryOutputDirectory() =
    Environment.DIRECTORY_PICTURES + "/" + getAppName() + "/" + "story"

fun Activity.getAppOutputDirectory() =
    Environment.DIRECTORY_PICTURES + "/" + getAppName()

private fun randomInteger() = (0 to 100).toList().random()

@Suppress("DEPRECATION")
fun Activity.saveImageToStorage(
    bitmap: Bitmap,
    filename: String = "${randomInteger()}.jpg",
    mimeType: String = "image/jpeg",
    directory: String = getAppOutputDirectory(),
    mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
) {
    val imageOutStream: OutputStream
    // android 10 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, directory)
        }
        contentResolver.run {
            val uri =
                contentResolver.insert(mediaContentUri, values)
                    ?: return
            imageOutStream = openOutputStream(uri) ?: return
        }
    } else {
        //saveGif(bitmap)
        val f = File(Environment.getExternalStorageDirectory(), directory)
        if (!f.exists()) {
            f.mkdirs()
        }
        val imagePath = f.absolutePath
        val image = File(imagePath, "$filename.jpeg")
        imageOutStream = FileOutputStream(image)
    }
    imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
}

/*fun generateGIF(array: ArrayList<Bitmap>): ByteArray? {
    val bitmaps: ArrayList<Bitmap> = array
    val bos = ByteArrayOutputStream()
    val encoder = AnimatedGifEncoder()
    encoder.start(bos)
    for (bitmap in bitmaps) {
        encoder.addFrame(bitmap)
    }
    encoder.finish()
    return bos.toByteArray()
}*/

fun Activity.saveGif(bitmap: Bitmap) {
    try {
        val outStream =
            FileOutputStream(
                "${
                    Environment
                        .getExternalStoragePublicDirectory(getAppOutputDirectory()).absolutePath
                }.gif"
            )
       /* outStream.write(generateGIF(arrayListOf(bitmap)))
        outStream.close()*/
    } catch (e: Exception) {
        e.printStackTrace()
    }
}