package com.mafqud.android.files
import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class StartUploadingResponse(
    @field:Json(name = "fields")
    val fields: Fields? = Fields(),
    @field:Json(name = "id")
    val id: Int? = 0, // 99
    @field:Json(name = "url")
    val url: String? = "" // https://s3.eu-south-1.amazonaws.com/mafqud-bucket
) {
    @Keep
    data class Fields(
        @field:Json(name = "acl")
        val acl: String? = "", // public-read
        @field:Json(name = "Content-Type")
        val contentType: String? = "", // image/png
        @field:Json(name = "key")
        val key: String? = "", // files/2baff4e4830a4217bb1511fd68884313.png
        @field:Json(name = "policy")
        val policy: String? = "", // eyJleHBpcmF0aW9uIjogIjIwMjItMDYtMDNUMjE6MTM6MjVaIiwgImNvbmRpdGlvbnMiOiBbeyJhY2wiOiAicHVibGljLXJlYWQifSwgeyJDb250ZW50LVR5cGUiOiAiaW1hZ2UvcG5nIn0sIFsiY29udGVudC1sZW5ndGgtcmFuZ2UiLCAxLCAxMDQ4NTc2MF0sIHsiYnVja2V0IjogIm1hZnF1ZC1idWNrZXQifSwgeyJrZXkiOiAiZmlsZXMvMmJhZmY0ZTQ4MzBhNDIxN2JiMTUxMWZkNjg4ODQzMTMucG5nIn0sIHsieC1hbXotYWxnb3JpdGhtIjogIkFXUzQtSE1BQy1TSEEyNTYifSwgeyJ4LWFtei1jcmVkZW50aWFsIjogIkFLSUFTTFRXRzY2Qko2VkQ1N1hTLzIwMjIwNjAzL2V1LXNvdXRoLTEvczMvYXdzNF9yZXF1ZXN0In0sIHsieC1hbXotZGF0ZSI6ICIyMDIyMDYwM1QyMTA4MjVaIn1dfQ==
        @field:Json(name = "x-amz-algorithm")
        val xAmzAlgorithm: String? = "", // AWS4-HMAC-SHA256
        @field:Json(name = "x-amz-credential")
        val xAmzCredential: String? = "", // AKIASLTWG66BJ6VD57XS/20220603/eu-south-1/s3/aws4_request
        @field:Json(name = "x-amz-date")
        val xAmzDate: String? = "", // 20220603T210825Z
        @field:Json(name = "x-amz-signature")
        val xAmzSignature: String? = "" // febab93cbf322e4b925b4f5803960bf1fa95ab27d3c9455d758f8ffff17a4edd
    )
}