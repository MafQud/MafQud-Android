package com.mafqud.android.util.network

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

data class ErrorResponse(
    val message: String
)

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()

    sealed class NetworkError : Result<Nothing>() {
        object NoInternet : NetworkError()
        data class Generic(val code: Int? = null, val error: ErrorResponse = ErrorResponse("")) :
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
            when (throwable) {
                is IOException -> Result.NetworkError.NoInternet
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Result.NetworkError.Generic(code, errorResponse)
                }
                else -> {
                    Result.NetworkError.Generic(null)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse {
    return try {
        throwable.message?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }!!
    } catch (exception: Exception) {
        ErrorResponse("")
    }
}