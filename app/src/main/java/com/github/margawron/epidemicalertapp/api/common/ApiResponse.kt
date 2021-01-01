package com.github.margawron.epidemicalertapp.api.common

import java.lang.StringBuilder

sealed class ApiResponse<out T : Any> {
    data class Success<T : Any>(val body: T?) : ApiResponse<T>()
    data class Error(var code: Int?, val errors: List<ApiError>) :
        ApiResponse<Nothing>()

    companion object{
        fun errorToMessage(errors: Error): StringBuilder {
            val error = StringBuilder()
            errors.errors.joinTo(
                error,
                postfix = "\n"
            ){ e -> e.errorMessage }
            return error
        }
    }
}

data class ApiError(val errorName: String, val errorField: String?, val errorMessage: String)