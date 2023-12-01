package ru.ok.itmo.example.channelListPart

object ChannelListModel {
    private var channels: List<String>? = null

    suspend fun getChannels(): GetChannelListState{
        val channelListState = ChannelListResponseChecker.safeGetChannels()
        if(channelListState is GetChannelListState.Success)
            channels = channelListState.channels
        return channelListState
    }

}