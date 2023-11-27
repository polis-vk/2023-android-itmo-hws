package ru.ok.itmo.tamtam.server

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class Info {
    companion object {
        const val SERVER_URL = "https://faerytea.name:8008/"
    }
}

data class LoginRequest(
    @SerializedName("name")
    val login: String,

    @SerializedName("pwd")
    val password: String
)

interface ServerApi {
    @POST("/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body requestBody: LoginRequest): ResponseBody
}

interface ServerApiWithInterceptor {
    @GET("/1ch")
    suspend fun getMessagesFrom1ch(): ResponseBody

    @GET("/channels")
    suspend fun getChannels(): ResponseBody

    @POST("/logout")
    suspend fun logout(): ResponseBody
}

object RetrofitClient {
    val apiService: ServerApi by lazy {
        Retrofit.Builder()
            .baseUrl(Info.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerApi::class.java)
    }
}

object RetrofitClientWithInterceptor {
    val apiService: ServerApiWithInterceptor by lazy {
        Retrofit.Builder()
            .baseUrl(Info.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build())
            .build()
            .create(ServerApiWithInterceptor::class.java)
    }
}