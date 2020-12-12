package com.github.margawron.epidemicalertapp.api

import com.fasterxml.jackson.annotation.JsonIgnore

sealed class ApiResponse<out T : Any> {
    data class SuccessWithBody<T : Any>(val body: T) : ApiResponse<T>()
    data class SuccessWithoutBody(val code: Int, val message: String) : ApiResponse<Nothing>()
    data class Error(val errorName: String, val errorField: String?, val errorMessage: String, @JsonIgnore var code: Int? = null) :
        ApiResponse<Nothing>()
}