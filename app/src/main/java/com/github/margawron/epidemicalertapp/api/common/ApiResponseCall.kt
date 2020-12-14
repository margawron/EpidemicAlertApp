package com.github.margawron.epidemicalertapp.api.common

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class ApiResponseCall<T : Any>(
    private val call: Call<T>,
    private val errorBodyConverter: Converter<ResponseBody, List<ApiError>>
) : Call<ApiResponse<T>> {

    override fun clone() = ApiResponseCall(call.clone(), errorBodyConverter)

    override fun execute() = throw UnsupportedOperationException("Cannot execute ApiResponseCall")

    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        return call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(ApiResponse.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(
                                null
                            )
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorBodyConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(ApiResponse.Error(code, errorBody))
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(
                                ApiResponse.Error(
                                    null,
                                    listOf(
                                        ApiError(
                                            "Unknown error",
                                            "",
                                            error?.string() ?: "Nieznany błąd"
                                        )

                                    )
                                )
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> {
                        ApiResponse.Error(
                            null,
                            listOf(
                                ApiError(
                                    "Network Error",
                                    "none",
                                    throwable.localizedMessage ?: "Doszło do błędu z połączeniem"
                                )
                            )
                        )
                    }
                    else -> {
                        ApiResponse.Error(
                            null,
                            listOf(
                                ApiError(
                                    "Unknown Error",
                                    "none",
                                    throwable.localizedMessage ?: "Nieznany błąd"
                                )
                            )
                        )
                    }
                }
                callback.onResponse(this@ApiResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled() = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

}