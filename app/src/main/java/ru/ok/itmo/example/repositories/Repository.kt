package ru.ok.itmo.example.repositories

import android.util.Log
import ru.ok.itmo.example.models.Channel
import ru.ok.itmo.example.models.LoginRequest
import ru.ok.itmo.example.models.LoginResponse
import ru.ok.itmo.example.models.Message
import ru.ok.itmo.example.models.Text
import ru.ok.itmo.example.models.sendData
import ru.ok.itmo.example.models.sendMessage
import ru.ok.itmo.example.network.MainApi
import ru.ok.itmo.example.network.RetrofitInitialization
import ru.ok.itmo.example.room.TokenDao
import ru.ok.itmo.example.room.entities.TokenEntity

class Repository(private val tokenDao: TokenDao? = null) {

    val token = ""
    private var lastKnownId: HashMap<String, Long> = hashMapOf();
    private lateinit var mainApi: MainApi;

    fun initRetrofit() {
        mainApi = RetrofitInitialization().getClient().create(MainApi::class.java)
    }

    fun logIn(
        loginRequest: LoginRequest
    ): LoginResponse {
        val logInResponse = mainApi.logIn(loginRequest).execute()
        Log.i("returnCode", logInResponse.code().toString())
        when (logInResponse.code()) {
            200 -> {
                tokenDao!!.insert(TokenEntity(0, logInResponse.body()))
                Log.i("Room Logout", tokenDao.getToken()[0].token.toString())
                return LoginResponse(logInResponse.body(), logInResponse.code())
            }

            else -> {
                tokenDao!!.update(TokenEntity(0, ""))
                return LoginResponse("", logInResponse.code())
            }
        }
    }

    fun logout() {
        mainApi.logout(tokenDao?.getToken()?.get(0)?.token.toString()).execute()
        tokenDao!!.delete(TokenEntity(0, token))
    }

    fun postMessage(text: String, where: String) {
        mainApi.postMessage(
            tokenDao?.getToken()?.get(0)?.token.toString(),
            sendMessage("LetItBeRickAstley", "${where}@channel", sendData(Text(text)))
        )
            .execute()
    }

    fun isLoggedIn(): Boolean {
        if (tokenDao?.getToken()?.size != 0) {
            Log.i(
                "Repository isloggedIn",
                (tokenDao?.getToken()?.get(0)?.token.toString() != "").toString()
            )
            return tokenDao?.getToken()?.get(0)?.token.toString() != ""
        } else {
            return false
        }
    }

    fun getChannel(name: String = "1"): List<Message>? {
        val request = mainApi.getChannel(name, lastKnownId[name]).execute()
        when (request.code()) {
            200 -> {
                if (request.body()!!.isEmpty()) {
                    return request.body()
                } else {
                    lastKnownId[name] = request.body()!![request.body()!!.size - 1].id!!
                    return request.body()
                }
            }

            else -> throw InvalidDataException("Invalid Data Recieved")
        }
    }

    fun isLast(key: String): Boolean {
        return lastKnownId[key] == getFirstMessage(key)!!.id!!.toLong()
    }

    fun getInbox(): MutableList<Channel> {
        val inboxChannels: MutableList<Channel> = mutableListOf()
        val map: HashMap<String, Int> = hashMapOf("1@channel" to 1)
        val request = mainApi.getInbox(tokenDao?.getToken()?.get(0)?.token.toString()).execute()
        when (request.code()) {
            200 -> {
                val inboxMessages = request.body();
                if (inboxMessages != null) {
                    for (message in inboxMessages) {
                        when (message.from) {
                            "LetItBeRickAstley" -> {
                                if (map.containsKey(message.to)) {
                                    continue
                                } else {
                                    map[message.to.toString()] = 1
                                }
                            }

                            else -> {
                                if (map.containsKey(message.from)) {
                                    continue
                                } else {
                                    map[message.from.toString()] = 1
                                }
                            }
                        }
                    }
                }
                for ((key, value) in map) {
                    lastKnownId[key.removeSuffix("@channel")] = 1000000
                    val tmpChannel = Channel(key, getLastMessage(key))
                    inboxChannels.add(tmpChannel)
                }

                return inboxChannels;
            }

            else -> throw InvalidDataException("Invalid Data Recieved")
        }
    }

    fun getLastMessage(channel: String): Message? {
        return mainApi.getLastMessage(tokenDao?.getToken()?.get(0)?.token.toString(), channel)
            .execute().body()
            ?.get(0);
    }

    fun getFirstMessage(channel: String): Message? {
        Log.i("hashMap", lastKnownId[channel].toString())
        return mainApi.getFirstMessage(tokenDao?.getToken()?.get(0)?.token.toString(), channel)
            .execute().body()
            ?.get(0);
    }

}

class InvalidDataException(s: String) : Throwable(s)
