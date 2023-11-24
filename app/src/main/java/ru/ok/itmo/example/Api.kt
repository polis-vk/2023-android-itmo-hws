package ru.ok.itmo.example

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    companion object {
        const val BASE_URL = "http://dummy.restapiexample.com"
    }

    @GET("data_chat")
    fun getReq(): Single<DataChat>

    @POST("data_chat")
    fun postReq(@Body dataChar: DataChat): Call<DataChat>
}