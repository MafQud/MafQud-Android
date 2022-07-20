package com.mafqud.android.report.uploading

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.files.FinishUploadBody
import com.mafqud.android.files.StartUploadBody
import com.mafqud.android.files.StartUploadingResponse
import com.mafqud.android.home.model.CaseType
import com.mafqud.android.report.uploading.models.CreateCaseBody
import com.mafqud.android.util.ImageSaver
import com.mafqud.android.util.fileManager.getCurrentDate
import com.mafqud.android.util.fileManager.getCurrentDateMilliSec
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.FileUtils
import com.mafqud.android.util.other.LogMe
import com.mafqud.android.util.other.MultiPartUtil
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.lang.Exception
import javax.inject.Inject


class UploadingRepository @Inject constructor() : BaseRepository() {

    /**
     * all logic is here
     *  1- call startUploadingImage() to get credentials.
     *  2- try upload the image to S3 with the credentials.
     *  3- after success: call finishUploadingImage() to confirm uploading.
     */
    suspend fun uploadImages(imagesUris: List<Uri>): Result<UploadImagesIDs?> {
        return safeApiCall {
            val firstImage = imagesUris.first()
            val firstThumbnail = getImageThumbnail(firstImage)
            LogMe.i("firstThumbnail", firstThumbnail.path.toString())
            LogMe.i("imagesUris", imagesUris.size.toString())


            val allImagesToUpload = mutableListOf<Uri>()
            allImagesToUpload.add(firstThumbnail)
            allImagesToUpload.addAll(imagesUris)
            LogMe.i("allImagesToUpload", allImagesToUpload.size.toString())

            val uploadedFilesIds = listOf<Int>().toMutableList()

            allImagesToUpload.forEach { uri ->
                LogMe.i("myUri", uri.toString())

                // First -> get uploaded file credentials
                // get the credentials to upload photo

                val fileName = uri.path.toString()
                val imageMIMEType = getMIMEType(uri)
                val imageExtension = "." + imageMIMEType.substringAfter('/')

                LogMe.i("fileName", fileName)
                val awsCredentials = remoteDataManager.startUploadingImage(
                    StartUploadBody(
                        // fileName : myImagePickedName/21.jpg
                        fileName = fileName,
                        // fileType : image/jpg
                        fileType = imageMIMEType
                    )
                )


                LogMe.i("multiPartPhoto", uri.toString())
                val multiPartPhoto: MultipartBody.Part? = try {
                    MultiPartUtil.fileToMultiPart(appContext, uri, "file")
                } catch (e: Exception) {
                    LogMe.i("multiPartPhoto", e.message.toString())
                    null
                }

                val mediaType = "multipart/form-data".toMediaType()
                val uploadingResult = awsUploading.uploadFileToAWS(
                    acl = awsCredentials.fields?.acl.toString().toRequestBody(mediaType),
                    Content_Type = awsCredentials.fields?.contentType.toString()
                        .toRequestBody(mediaType),
                    key = (awsCredentials.fields?.key.toString())
                        .toRequestBody(mediaType),
                    x_amz_algorithm = awsCredentials.fields?.xAmzAlgorithm.toString()
                        .toRequestBody(mediaType),
                    x_amz_credential = awsCredentials.fields?.xAmzCredential.toString()
                        .toRequestBody(mediaType),
                    x_amz_date = awsCredentials.fields?.xAmzDate.toString()
                        .toRequestBody(mediaType),
                    policy = awsCredentials.fields?.policy.toString().toRequestBody(mediaType),
                    x_amz_signature = awsCredentials.fields?.xAmzSignature.toString()
                        .toRequestBody(mediaType),
                    file = multiPartPhoto!!
                )

                LogMe.i("uploadingResult", uploadingResult.message())
                LogMe.i("uploadingResult", uploadingResult.body().toString())
                LogMe.i("uploadingResult", uploadingResult.errorBody().toString())

                val resultFinish = remoteDataManager.finishUploadingImage(
                    FinishUploadBody(fileId = awsCredentials.id.toString())
                )

                LogMe.i("uploadedFileId", resultFinish.id.toString())
                uploadedFilesIds.add(resultFinish.id)
            }
            return@safeApiCall UploadImagesIDs(
                thumbnailId = uploadedFilesIds.first(),
                imagesIds = uploadedFilesIds.drop(1)
            )
        }
    }

    private fun getUriMemiType(uri: Uri): String {
        val contentResolver = appContext.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val result =
            "${mime.getExtensionFromMimeType(contentResolver.getType(uri)) ?: "*"}"
        LogMe.i("getUriMemiType", result)
        return result
    }

    private fun getMIMEType(uri: Uri): String {
        var mimeType: String? = null
        mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr = appContext.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri
                    .toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase()
            )
        }
        if (mimeType == null) {
            FirebaseCrashlytics.getInstance()
                .log("UploadingRepository: getMEMIType-> mimeType is null")
        }
        LogMe.i("getUriMIMEType", mimeType.toString())
        return mimeType ?: "notDefined"
    }


    private fun getImageThumbnail(firstImage: Uri): Uri {
        val THUMBSIZE = 128

        val thumbImage = ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeFile(FileUtils.getPath(appContext, firstImage)),
            THUMBSIZE,
            THUMBSIZE
        )

        val thumbnailName = getCurrentDate()
        ImageSaver(appContext)
            .setFileName(thumbnailName)
            .setExternal(false)
            .save(thumbImage)

        val bitmap =
            ImageSaver(appContext).setExternal(false).setFileName(thumbnailName).load()

        return getImageUri(appContext, bitmap)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = appContext.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private suspend fun uploadToS3Service(
        imagesUris: List<Uri>,
        data: StartUploadingResponse
    ): UploadImagesIDs? {
        val resultS3 = safeApiCall {
            val multiPartPhoto =
                MultiPartUtil.fileToMultiPart(appContext, imagesUris[0], "file")

            val mediaType = "multipart/form-data".toMediaType()
            return@safeApiCall awsUploading.uploadFileToAWS(
                acl = data.fields?.acl.toString().toRequestBody(mediaType),
                Content_Type = data.fields?.contentType.toString().toRequestBody(mediaType),
                key = data.fields?.key.toString().toRequestBody(mediaType),
                x_amz_algorithm = data.fields?.xAmzAlgorithm.toString().toRequestBody(mediaType),
                x_amz_credential = data.fields?.xAmzCredential.toString().toRequestBody(mediaType),
                x_amz_date = data.fields?.xAmzDate.toString().toRequestBody(mediaType),
                policy = data.fields?.policy.toString().toRequestBody(mediaType),
                x_amz_signature = data.fields?.xAmzSignature.toString().toRequestBody(mediaType),
                file = multiPartPhoto
            )
        }

        return when (resultS3) {
            is Result.NetworkError.Generic, Result.NetworkError.NoInternet -> null
            is Result.Success -> finishUploading(data.id)
        }
    }

    private suspend fun finishUploading(fileID: Int?): UploadImagesIDs? {
        val resultFinish = safeApiCall {
            return@safeApiCall remoteDataManager.finishUploadingImage(
                FinishUploadBody(fileID.toString())
            )
        }
        return when (resultFinish) {
            is Result.NetworkError.Generic, Result.NetworkError.NoInternet -> null
            is Result.Success -> UploadImagesIDs(
                thumbnailId = fileID ?: -1,
                imagesIds = listOf(fileID ?: -1),
            )
        }
    }

    data class UploadImagesIDs(
        val thumbnailId: Int,
        val imagesIds: List<Int>,
    )


    suspend fun uploadCase(
        caseItem: CreateCaseBody,
        uploadFilesId: UploadImagesIDs
    ): Result<Boolean> {
        return safeApiCall {
            // modify case
            val case = caseItem.apply {
                thumbnail = uploadFilesId.thumbnailId
                fileIds = uploadFilesId.imagesIds
                type = when (caseType) {
                    CaseType.FOUND -> "F"
                    CaseType.MISSING -> "M"
                    CaseType.NONE -> ""
                }
            }
            // submit case

            val res = safeApiCall {
                return@safeApiCall remoteDataManager.uploadCase(case)
            }
            return@safeApiCall when (res) {
                is Result.NetworkError.Generic,
                Result.NetworkError.NoInternet -> false
                is Result.Success -> true
            }
        }

    }
}