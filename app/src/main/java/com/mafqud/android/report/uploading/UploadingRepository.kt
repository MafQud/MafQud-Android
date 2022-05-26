package com.mafqud.android.report.uploading

import android.net.Uri
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.files.FinishUploadBody
import com.mafqud.android.files.StartUploadBody
import com.mafqud.android.files.StartUploadingResponse
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.MultiPartUtil
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class UploadingRepository @Inject constructor() : BaseRepository() {

    /**
     * all logic is here
     *  1- call startUploadingImage() to get credentials.
     *  2- try upload the image to S3 with the credentials.
     *  3- after success: call finishUploadingImage() to confirm uploading.
     */
    suspend fun uploadImages(imagesUris: List<Uri>): Result<Boolean> {
        return safeApiCall {
            val imageName = imagesUris.get(0)
            return@safeApiCall coroutineScope {
                val resultStart = safeApiCall {
                    return@safeApiCall remoteDataManager.startUploadingImage(
                        StartUploadBody(
                            fileName = imageName.path.toString()
                        )
                    );
                }
                return@coroutineScope when (resultStart) {
                    is Result.NetworkError.Generic,
                    Result.NetworkError.NoInternet -> return@coroutineScope false
                    is Result.Success -> uploadToS3Service(imagesUris, resultStart.data)
                }
            }
        }
    }

    private suspend fun uploadToS3Service(
        imagesUris: List<Uri>,
        data: StartUploadingResponse
    ): Boolean {
        val resultS3 = safeApiCall {
            val multiPart =
                MultiPartUtil.fileToMultiPart(appContext, imagesUris.get(0), "image")
            return@safeApiCall remoteDataManager.uploadImageToS3(
                contentType = data.fields?.contentType ?: "image/*",
                uploadUrl = data.url ?: "",
                file = multiPart
            )
        }
        return when (resultS3) {
            is Result.NetworkError.Generic, Result.NetworkError.NoInternet -> false
            is Result.Success -> finishUploading(data.id)
        }
    }

    private suspend fun finishUploading(fileID: Int?): Boolean {
        val resultFinish = safeApiCall {
            return@safeApiCall remoteDataManager.finishUploadingImage(
                FinishUploadBody(fileID.toString())
            )
        }
        return when (resultFinish) {
            is Result.NetworkError.Generic, Result.NetworkError.NoInternet -> false
            is Result.Success -> true
        }
    }


    suspend fun uploadCase(caseItem: CaseItem?): Result<String> {
        return safeApiCall {
            return@safeApiCall ""
        }
    }
}