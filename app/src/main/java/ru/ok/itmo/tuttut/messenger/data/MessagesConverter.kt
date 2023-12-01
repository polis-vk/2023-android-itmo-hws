package ru.ok.itmo.tuttut.messenger.data

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.ok.itmo.tuttut.messenger.domain.Message

class MessagesConverter {
    @TypeConverter
    fun fromMessageList(value: List<Message>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMessageList(value: String): List<Message> {
        return Json.decodeFromString(value)
    }
}