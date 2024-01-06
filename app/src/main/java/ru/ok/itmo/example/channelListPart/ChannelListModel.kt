package ru.ok.itmo.example.channelListPart

import ru.ok.itmo.example.api_logic.ChanChatAPI
import ru.ok.itmo.example.channelListPart.states.GetChannelListState
import ru.ok.itmo.example.channelListPart.states.GetChannelNamesState
import ru.ok.itmo.example.channelListPart.states.GetOneChannelMessagesState
import java.util.Date

class ChannelListModel {
    private val chanChatAPI = ChanChatAPI.provideChanChatAPI

    suspend fun getChannelNames(): GetChannelNamesState {
        return try{
            val response = chanChatAPI.getChannelNames()
            if(response.isSuccessful && response.body() != null){
                GetChannelNamesState.Success(response.body()!!)
            }else{
                GetChannelNamesState.Failed
            }
        } catch (e: Exception){
            GetChannelNamesState.Failed
        }
    }

    suspend fun getOneChannelMessages(channelName: String): GetOneChannelMessagesState{
        return try{
            val response = chanChatAPI.getOneChannelMessages(channelName)
            if(response.isSuccessful && response.body() != null){
                GetOneChannelMessagesState.Success(response.body()!!)
            }else{
                GetOneChannelMessagesState.Failed
            }
        } catch (e: Exception){
            GetOneChannelMessagesState.Failed
        }
    }

    suspend fun getChannelList(): GetChannelListState{
        when(val getNames = getChannelNames()){
            is GetChannelNamesState.Failed -> return GetChannelListState.Failed("Не удалось получить имена чатов")
            is GetChannelNamesState.Success -> {
                val result: MutableList<ChannelDataClass> = mutableListOf()
                for(name in getNames.channels){
                    when(val getMessages = getOneChannelMessages(name)){
                        is GetOneChannelMessagesState.Failed -> return GetChannelListState.Failed("Не удалось получить список сообщений для чата: $name")
                        is GetOneChannelMessagesState.Success -> {
                            val lastMessage = getMessages.messages[0] //Проверить, 0 или size()-1
                            result.add(ChannelDataClass(name, lastMessage.data.Text.text, Date(lastMessage.time.toLong() * 1000), ""))
                        }
                        else -> {}
                    }
                }
                return GetChannelListState.Success(result)
            }
            else -> {return GetChannelListState.Failed("Непредвиденное поведение при получении списка сообщений")}
        }
    }
}