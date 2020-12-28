package com.htec.task.repository.retrofit

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NetworkHelper {
    companion object {
        suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
            return withContext(dispatcher) {
                try {
                    ResultWrapper.Success(apiCall.invoke())
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> ResultWrapper.NetworkError
                        is HttpException -> {
                            val code = throwable.code()
                            val errorResponse = convertErrorBody(throwable)
                            ResultWrapper.GenericError(code, errorResponse)
                        }
                        else -> {
                            ResultWrapper.GenericError(null, null)
                        }
                    }
                }
            }
        }

        private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
            return try {
                throwable.response()?.errorBody()?.source()?.let {
                    //TODO Handle server's error codes
                    null
                }
            } catch (exception: Exception) {
                null
            }
        }
    }
}