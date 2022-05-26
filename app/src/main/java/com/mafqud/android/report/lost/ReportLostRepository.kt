package com.mafqud.android.report.lost

import com.mafqud.android.auth.login.LoginIntent
import com.mafqud.android.auth.login.models.LoginBody
import com.mafqud.android.auth.login.models.LoginResponse
import com.mafqud.android.base.BaseRepository
import com.mafqud.android.util.network.Result
import com.mafqud.android.util.network.safeApiCall
import com.mafqud.android.util.other.LogMe
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import com.mafqud.android.util.other.getUserPayloadFromToken
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class ReportLostRepository @Inject constructor() : BaseRepository() {



}