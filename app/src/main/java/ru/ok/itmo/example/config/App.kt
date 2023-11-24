package ru.ok.itmo.example.config

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.ok.itmo.example.domain.AuthToken
import ru.ok.itmo.example.repository.ChannelRepository
import ru.ok.itmo.example.repository.UserRepository

class App: Application() {

    lateinit var userRepository: UserRepository
        private set
    lateinit var channelRepository: ChannelRepository
        private set
    var token: AuthToken? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        val client = OkHttpClient()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://faerytea.name:8008/")
            .build()
        userRepository = retrofit.create(UserRepository::class.java)
        channelRepository = retrofit.create(ChannelRepository::class.java)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
