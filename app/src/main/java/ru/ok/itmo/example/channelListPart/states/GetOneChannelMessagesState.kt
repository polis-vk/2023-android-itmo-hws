package ru.ok.itmo.example.channelListPart.states

import ru.ok.itmo.example.api_logic.api_models.messageJSON.MessageJSONModel

sealed class GetOneChannelMessagesState{
    data class Success(val messages: List<MessageJSONModel>): GetOneChannelMessagesState()
    data object Failed : GetOneChannelMessagesState()
}
