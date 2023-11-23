package ru.ok.itmo.example.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainRepository {

    val server = "https://faerytea.name:8008"
    var user: User? = null

    enum class LOADING_STATUS {
        LOADING,
        LOADED,
        ERROR
    }

    val channels = MutableLiveData<Array<String>>()
    val channelsLoadingStatus = MutableLiveData<LOADING_STATUS>()

    fun buildURL(vararg paths: String): URL {
        return Uri.parse(server).buildUpon().run {
            paths.forEach { appendPath(it) }
            URL(toString())
        }
    }

    fun updateChannels() {
        if (channelsLoadingStatus.value == LOADING_STATUS.LOADING) return
        val url = buildURL("channels")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                (url.openConnection() as HttpURLConnection).run {
                    requestMethod = "GET"

                    val code = responseCode
                    val response = inputStream.bufferedReader().readText()

                    launch(Dispatchers.Main) {
                        if (code == 200) {
                            val jsonArray = JSONArray(response)
                            channels.value = Array(jsonArray.length()) {
                                jsonArray.getString(it).dropLast(8)
                            }
                            channelsLoadingStatus.value = LOADING_STATUS.LOADED
                        } else {
                            channelsLoadingStatus.value = LOADING_STATUS.ERROR
                        }
                    }
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    channelsLoadingStatus.value = LOADING_STATUS.ERROR
                }
            }
        }
    }

    fun verifyAndLogin(
        login: String,
        password: String,
        signIn: MutableLiveData<Boolean>,
        errorCallback: (Int?, Exception?)->Unit
    ) {
        val requestBody = "{ \"name\": \"$login\",  \"pwd\": \"$password\" }".toByteArray()
        val url = buildURL("login")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                (url.openConnection() as HttpURLConnection).run {
                    requestMethod = "POST"
                    outputStream.write(requestBody)

                    val code = responseCode
                    var token: String = ""
                    if (code == 200) {
                        token = inputStream.bufferedReader().readLine()
                    }
                    launch(Dispatchers.Main) {
                        if (code == 200) {
                            launch(Dispatchers.Main) {
                                user = User(token)
                                signIn.value = true
                            }
                        } else {
                            errorCallback(code, null)
                        }
                    }
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    errorCallback(null, e)
                }
            }
        }
    }

    fun getChannelMessages(
        channel: String,
        callback: (Array<Message>?)->Unit,
        limit: Int = 20,
        lastKnownId: Int = 0,
        reverse: Boolean = false
    ) {
        val url = buildURL("channel", channel)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                (url.openConnection() as HttpURLConnection).run {
                    requestMethod = "GET"
                    addRequestProperty("limit", limit.toString())
                    addRequestProperty("lastKnownId", lastKnownId.toString())
                    addRequestProperty("reverse", reverse.toString())

                    val code = responseCode
                    val response = inputStream.bufferedReader().readText()

                    launch(Dispatchers.Main) {
                        if (code == 200) {
                            val jsonArray = JSONArray(response)
                            callback(
                                Array(jsonArray.length()) {
                                    Message.parseJSON(jsonArray.getJSONObject(it))
                                }
                            )
                        } else {
                            callback(null)
                        }
                    }
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }

    fun logout() {
        val token = user?.token ?: return
        val url = buildURL("logout")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                (url.openConnection() as HttpURLConnection).run {
                    requestMethod = "POST"
                    addRequestProperty("X-Auth-Token", token)
                }
            } catch (e: IOException) {
                // logout wasn't successful
            }
        }
        user = null
    }
}