package com.mafqud.android.report.uploading

import android.net.Uri
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject


class UploadingRepository @Inject constructor(private val remoteData: RemoteDataManager) {

    suspend fun uploadImages(imagesUris: List<Uri>, success: Boolean = false): Result<List<String>> {
        return safeApiCall {
            delay(3000)
            if (!success) {
                throw IOException()
            }
            return@safeApiCall listOf("images1.png")
        }
    }


    suspend fun uploadCase(caseItem: CaseItem?, success: Boolean = false): Result<String> {
        return safeApiCall {
            delay(3000)
            if (!success) {
                throw IOException()
            }
            return@safeApiCall ""
        }
    }
}