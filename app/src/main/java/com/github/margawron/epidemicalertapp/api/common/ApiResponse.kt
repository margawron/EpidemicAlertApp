package com.github.margawron.epidemicalertapp.api.common

sealed class ApiResponse<out T : Any> {
    data class Success<T : Any>(val body: T?) : ApiResponse<T>()
    data class Error(var code: Int?, val errors: List<ApiError>) :
        ApiResponse<Nothing>()
}

data class ApiError(val errorName: String, val errorField: String?, val errorMessage: String)