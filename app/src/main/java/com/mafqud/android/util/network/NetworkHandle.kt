package com.mafqud.android.util.network

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.auth0.android.jwt.DecodeException
import com.mafqud.android.R
import com.mafqud.android.util.other.LogMe
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

data class ErrorResponse(
    val message: String
)

sealed class HttpErrorType {
    object BadRequest : HttpErrorType()
    object NotAuthorized : HttpErrorType()
    object Forbidden : HttpErrorType()
    object NotFound : HttpErrorType()
    data class DataInvalid(val errorResponseModel: ErrorResponseModel) : HttpErrorType()
    object InternalServerError : HttpErrorType()
    object BadGateway : HttpErrorType()
    object Unknown : HttpErrorType()

}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()

    sealed class NetworkError : Result<Nothing>() {
        object NoInternet : NetworkError()
        data class Generic(
            val type: HttpErrorType = HttpErrorType.Unknown,
            val code: Int? = null, val error: ErrorResponseModel = ErrorResponseModel(message = "")
        ) :
            NetworkError()
    }
}

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            getNetworkErrorFromThrowable(throwable)
        }
    }
}

fun getNetworkErrorFromThrowable(throwable: Throwable): Result.NetworkError {
    return when (throwable) {
        is IOException -> Result.NetworkError.NoInternet
        is HttpException -> {
            val code = throwable.code()
            val errorResponse = convertErrorBody(throwable)
            LogMe.i("statusCode", code.toString())
            val httpErrorType = when (code) {
                400 -> HttpErrorType.BadRequest
                401 -> HttpErrorType.NotAuthorized
                403 -> HttpErrorType.Forbidden
                404 -> HttpErrorType.NotFound
                422 -> HttpErrorType.DataInvalid(errorResponse)
                500 -> HttpErrorType.InternalServerError
                502 -> HttpErrorType.BadGateway
                else -> HttpErrorType.Unknown
            }
            Result.NetworkError.Generic(httpErrorType, code, errorResponse)
        }
        else -> {
            Result.NetworkError.Generic()
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponseModel {
    return try {
        val errorBody = throwable.response()?.errorBody()?.string() ?: ""

        Log.i("convertErrorBody: ", throwable.code().toString())
        Log.i("convertErrorBody: ", errorBody)
        val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponseModel::class.java)
        moshiAdapter.fromJson(errorBody) ?: ErrorResponseModel(message = "")
    } catch (exception: Exception) {
        ErrorResponseModel(message = "")
    }
}

@Composable
fun getErrorType(type: HttpErrorType): String {
    LogMe.i("errorType", type.toString())
    return when (type) {
        HttpErrorType.BadGateway -> stringResource(id = R.string.error_bad_gateway)
        HttpErrorType.BadRequest -> stringResource(id = R.string.error_bad_request)
        HttpErrorType.Forbidden -> stringResource(id = R.string.error_forbidden)
        HttpErrorType.InternalServerError -> stringResource(id = R.string.error_server_error)
        HttpErrorType.NotAuthorized -> stringResource(id = R.string.error_not_auth)
        HttpErrorType.NotFound -> stringResource(id = R.string.error_not_found)
        HttpErrorType.Unknown -> stringResource(id = R.string.error_unknown)
        is HttpErrorType.DataInvalid -> getErrorMessage(type.errorResponseModel)
    }
}


fun getErrorMessage(errorResponseModel: ErrorResponseModel): String {
    return errorResponseModel.message.toString()
}

fun Context.getErrorType(type: HttpErrorType): String {
    return when (type) {
        HttpErrorType.BadGateway -> getString(R.string.error_bad_gateway)
        HttpErrorType.BadRequest -> getString(R.string.error_bad_request)
        HttpErrorType.Forbidden -> getString(R.string.error_forbidden)
        HttpErrorType.InternalServerError -> getString(R.string.error_server_error)
        HttpErrorType.NotAuthorized -> getString(R.string.error_not_auth)
        HttpErrorType.NotFound -> getString(R.string.error_not_found)
        HttpErrorType.Unknown -> getString(R.string.error_unknown)
        is HttpErrorType.DataInvalid -> getErrorMessage(type.errorResponseModel)
    }
}