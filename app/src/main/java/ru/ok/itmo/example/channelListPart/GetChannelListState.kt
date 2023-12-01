package ru.ok.itmo.example.channelListPart

sealed class GetChannelListState{
    data class Success(val channels: List<String>): GetChannelListState()
    data class Failed(val error_messenge: String) : GetChannelListState()
    data object Loading : GetChannelListState()
    data object Empty : GetChannelListState()
}
