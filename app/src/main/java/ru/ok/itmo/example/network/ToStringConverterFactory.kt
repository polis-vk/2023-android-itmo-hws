package ru.ok.itmo.example.network

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ToStringConverterFactory : Converter.Factory() {
    companion object {
        val MEDIA_TYPE: MediaType = "text/plain".toMediaType()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (String::class.java == type) {
            return Converter {
                return@Converter it.string()
            }
        }
        return null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (String::class.java == type) {
            return Converter<String, RequestBody> {
                return@Converter it.toRequestBody(MEDIA_TYPE)
            }
        }
        return null
    }
}