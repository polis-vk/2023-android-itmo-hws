package ru.ok.itmo.tamtam.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.ok.itmo.tamtam.data.remote.ServerAPI

object Constants {
    // test data
    const val USER_LOGIN = "test"
    const val USER_PASSWORD = "StAtIctHe777"
    // end of test data

    const val TOKEN = "token"
    const val SHARED = "SH"
    private const val BASE_URL = "https://faerytea.name:8008/"


    // ::DISCLAIMER:: i could've use dagger, but i'm stupid
    private val mapper = JsonMapper
        .builder()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()
        .registerModule(KotlinModule.Builder().build())
    private val serverAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .build()
        .create(ServerAPI::class.java)

    fun getServerAPI(): ServerAPI = serverAPI
}