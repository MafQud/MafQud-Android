package com.mafqud.android.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.util.LinkedHashMap

interface AWSUploading {

    @Multipart
    @POST("/mafqud-bucket/")
    suspend fun uploadFileToAWS(
        //@PartMap params: LinkedHashMap<String, RequestBody>,
        @Part("acl") acl: RequestBody,
        @Part("Content-Type") Content_Type: RequestBody,
        @Part("key") key: RequestBody,
        @Part("x-amz-algorithm") x_amz_algorithm: RequestBody,
        @Part("x-amz-credential") x_amz_credential: RequestBody,
        @Part("x-amz-date") x_amz_date: RequestBody,
        @Part("policy") policy: RequestBody,
        @Part("x-amz-signature") x_amz_signature: RequestBody,
        @Part file: MultipartBody.Part,
    ) : Response<Unit>
}