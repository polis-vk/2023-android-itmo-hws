package ru.ok.itmo.example.channelListPart

import java.util.Date

data class ChannelDataClass(
    val title: String,
    val last_message: String,
    val time: Date,//Под вопросом
    //val image: String
)