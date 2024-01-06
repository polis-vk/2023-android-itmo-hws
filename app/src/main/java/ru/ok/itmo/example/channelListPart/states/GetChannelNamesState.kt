package ru.ok.itmo.example.channelListPart.states

sealed class GetChannelNamesState{
    data class Success(val channels: List<String>): GetChannelNamesState()
    data object Failed : GetChannelNamesState()
}
