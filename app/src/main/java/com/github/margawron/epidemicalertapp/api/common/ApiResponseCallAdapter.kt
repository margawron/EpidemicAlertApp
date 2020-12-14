package com.github.margawron.epidemicalertapp.api.common

import com.fasterxml.jackson.core.type.TypeReference
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ApiResponseCallAdapter : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation?>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ApiResponse<Foo>> or Call<ApiResponse<out Foo>>"
        }
        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != ApiResponse::class.java) {
            return null
        }

        check(responseType is ParameterizedType) { "Response must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<List<ApiError>>(null, object : TypeReference<List<ApiError>>(){}.type, annotations)

        return ApiResponseAdapter<Any>(successBodyType, errorBodyConverter)
    }


    class ApiResponseAdapter<T : Any>(
        private val servicedType: Type,
        private val errorConverter: Converter<ResponseBody, List<ApiError>>
    ) : CallAdapter<T, Call<ApiResponse<T>>> {
        override fun responseType(): Type = servicedType
        override fun adapt(call: Call<T>): Call<ApiResponse<T>> {
            return ApiResponseCall(call, errorConverter)
        }
    }
}