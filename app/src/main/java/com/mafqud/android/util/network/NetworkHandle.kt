package com.mafqud.android.util.network

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier.Companion.any
import androidx.compose.ui.res.stringResource
import com.auth0.android.jwt.DecodeException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mafqud.android.R
import com.mafqud.android.auth.register.models.ErrorSignUp
import com.mafqud.android.util.other.LogMe
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.logging.Logger

data class ErrorResponse(
    val message: String
)

sealed class HttpErrorType {
    data class BadRequest(val errorSignUp: ErrorSignUp? = null) : HttpErrorType()
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
            val myThrowable = throwable
            val code = myThrowable.code()
            var errorResponse = ErrorResponseModel()

            LogMe.i("statusCode", code.toString())
            val httpErrorType = when (code) {
                400 -> {
                    val error = getSignUpFormError(myThrowable)
                    HttpErrorType.BadRequest(error)
                }
                401 -> HttpErrorType.NotAuthorized
                403 -> HttpErrorType.Forbidden
                404 -> HttpErrorType.NotFound
                422 -> {
                    errorResponse = convertErrorBody(myThrowable)
                    HttpErrorType.DataInvalid(errorResponse)
                }
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

fun getSignUpFormError(throwable: HttpException): ErrorSignUp {
    return try {
        val errorBody = throwable.response()?.errorBody()?.string() ?: ""
        LogMe.i("getSignUpFormError: ", throwable.code().toString())
        LogMe.i("getSignUpFormError: ", errorBody)
        val moshiAdapter = Moshi.Builder().build().adapter(ErrorSignUp::class.java)
        moshiAdapter.fromJson(errorBody) ?: ErrorSignUp(message = "")
    } catch (exception: Exception) {
        ErrorSignUp(message = exception.message)
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponseModel {
    return try {
        val errorBody = throwable.response()?.errorBody()?.string() ?: ""

        LogMe.i("convertErrorBody: ", throwable.code().toString())
        LogMe.i("convertErrorBody: ", errorBody)
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
        is HttpErrorType.BadRequest -> getBadRequestMessage(type.errorSignUp)
        HttpErrorType.Forbidden -> stringResource(id = R.string.error_forbidden)
        HttpErrorType.InternalServerError -> stringResource(id = R.string.error_server_error)
        HttpErrorType.NotAuthorized -> stringResource(id = R.string.error_not_auth)
        HttpErrorType.NotFound -> stringResource(id = R.string.error_not_found)
        HttpErrorType.Unknown -> stringResource(id = R.string.error_unknown)
        is HttpErrorType.DataInvalid -> getErrorMessage(type.errorResponseModel)
    }
}

fun getBadRequestMessage(errorSignUp: ErrorSignUp?): String {
    if (errorSignUp?.detail == null) return ""
    val LOG = Logger.getLogger(ErrorSignUp::class.java.name)
    LogMe.i("getBadRequestMessage", errorSignUp.message.toString())
    return try {
        if (errorSignUp.detail.javaClass.kotlin.members.any {
                LogMe.i("fields", it.name)
                it.name == "detail" || it.name == "firebaseToken"
            }) {
            if (!errorSignUp.detail.username.isNullOrEmpty()) {
                errorSignUp.detail.username.first() ?: "Username not valid"

            } else if (!errorSignUp.detail.firebaseToken.isNullOrEmpty()) {
                    errorSignUp.detail.firebaseToken.first() ?: "Firebase token not valid"
                } else {
                    "Data not valid"
                }
            } else ""
        } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().log("NetworkHandle,getBadRequestMessage -> " + e.message)
        return "Can't get error message (e)"
    }
}


fun getErrorMessage(errorResponseModel: ErrorResponseModel): String {
    return errorResponseModel.message.toString()
}

fun Context.getErrorType(type: HttpErrorType): String {
    return when (type) {
        HttpErrorType.BadGateway -> getString(R.string.error_bad_gateway)
        is HttpErrorType.BadRequest -> getBadRequestMessage(type.errorSignUp)
        HttpErrorType.Forbidden -> getString(R.string.error_forbidden)
        HttpErrorType.InternalServerError -> getString(R.string.error_server_error)
        HttpErrorType.NotAuthorized -> getString(R.string.error_not_auth)
        HttpErrorType.NotFound -> getString(R.string.error_not_found)
        HttpErrorType.Unknown -> getString(R.string.error_unknown)
        is HttpErrorType.DataInvalid -> getErrorMessage(type.errorResponseModel)
    }
}