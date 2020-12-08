package com.github.margawron.epidemicalertapp.api

sealed class ApiResponse<out T : Any> {
    data class SuccessWithBody<T : Any>(val body: T) : ApiResponse<T>()
    data class SuccessWithoutBody(val code: Int, val message: String) : ApiResponse<Nothing>()
    data class Error(val errorName: String, val errorField: String, val errorMessage: String, var code: Int? = null) :
        ApiResponse<Nothing>()
}