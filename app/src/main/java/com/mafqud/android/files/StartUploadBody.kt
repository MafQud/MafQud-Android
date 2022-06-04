package com.mafqud.android.files

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * public static final String IMAGE_UNSPECIFIED = "image/*";
 * public static final String IMAGE_JPEG        = "image/jpeg";
 * public static final String IMAGE_JPG         = "image/jpg";
 * public static final String IMAGE_GIF         = "image/gif";
 * public static final String IMAGE_WBMP        = "image/vnd.wap.wbmp";
 * public static final String IMAGE_PNG         = "image/png";
*/
 **/

@Keep
data class StartUploadBody(
    @field:Json(name = "file_name")
    val fileName: String = System.currentTimeMillis().toString(), // test-image.png
    @field:Json(name = "file_type")
    val fileType: String = "image/png" // png
)

