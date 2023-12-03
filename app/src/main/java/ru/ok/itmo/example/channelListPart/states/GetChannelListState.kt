package ru.ok.itmo.example.channelListPart.states

import ru.ok.itmo.example.channelListPart.ChannelDataClass

sealed class GetChannelListState{
    data class Success(val channels: List<ChannelDataClass>): GetChannelListState()
    data class Failed(val error_messenge: String) : GetChannelListState()
    data object Loading : GetChannelListState()
    data object Empty : GetChannelListState()
}
