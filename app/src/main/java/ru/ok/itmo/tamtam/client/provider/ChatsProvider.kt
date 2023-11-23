package ru.ok.itmo.tamtam.client.provider

import ru.ok.itmo.tamtam.client.RetrofitProvider
import ru.ok.itmo.tamtam.client.api.ChatsApi
import ru.ok.itmo.tamtam.domain.ErrorType
import ru.ok.itmo.tamtam.dto.ChannelName
import ru.ok.itmo.tamtam.dto.Message

class ChatsProvider {
    private fun client() = ChatsApi.provideChatsApi(RetrofitProvider.retrofit())

    suspend fun getAllChannels(): Result<List<ChannelName>> {
        return try {
            val result = runCatching {
                client().getAllChannels()
            }
            val response = result.getOrNull() ?: return Result.failure(ErrorType.Unknown())
            return when (response.code()) {
                200 -> {
                    val list = response.body()
                    if (list != null) {
                        return Result.success(list)
                    } else {
                        return Result.failure(ErrorType.Unknown())
                    }
                }

                401 -> Result.failure(ErrorType.Unauthorized())
                else -> Result.failure(ErrorType.Unknown())
            }
        } catch (e: Exception) {
            Result.failure(ErrorType.InternetConnection())
        }
    }

    suspend fun getChannelMessages(name: ChannelName): Result<List<Message>> {
        return try {
            val result = runCatching {
                client().getChannelMessages(name)
            }
            val response = result.getOrNull() ?: return Result.failure(ErrorType.Unknown())
            return when (response.code()) {
                200 -> {
                    val list = response.body()
                    if (list != null) {
                        return Result.success(list)
                    } else {
                        return Result.failure(ErrorType.Unknown())
                    }
                }

                401 -> Result.failure(ErrorType.Unauthorized())
                else -> Result.failure(ErrorType.Unknown())
            }
        } catch (e: Exception) {
            Result.failure(ErrorType.InternetConnection())
        }
    }
}