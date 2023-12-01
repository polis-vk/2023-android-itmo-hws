package ru.ok.itmo.example.channelListPart

import ru.ok.itmo.example.api_logic.ChanChatAPI

object ChannelListResponseChecker {
    private val chanChatAPI = ChanChatAPI.provideChanChatAPI

    suspend fun safeGetChannels(): GetChannelListState{
        try{
            val response = chanChatAPI.getChannels()
            if(response.isSuccessful && response.body() != null){
                return GetChannelListState.Success(response.body()!!)
            }else{
                return GetChannelListState.Failed("Ничего не удалось найти")
            }
        } catch (e: Exception){
            return GetChannelListState.Failed("Упс. Что-то пошло не так")
        }
    }

    suspend fun safeGetChannelMessages(){
        
    }

}