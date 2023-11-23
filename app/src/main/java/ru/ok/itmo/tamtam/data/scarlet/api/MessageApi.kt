package ru.ok.itmo.tamtam.data.scarlet.api

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import ru.ok.itmo.tamtam.data.scarlet.model.EndTypingRequest
import ru.ok.itmo.tamtam.data.scarlet.model.NewMessageRequest
import ru.ok.itmo.tamtam.data.scarlet.model.NewMessageResponse
import ru.ok.itmo.tamtam.data.scarlet.model.StartTypingRequest
import ru.ok.itmo.tamtam.data.scarlet.model.TypingChangedResponse

interface MessageApi {
    @Receive
    fun observeEvents(): Flow<WebSocket.Event>

    @Receive
    fun observeNewMessage(): Flow<NewMessageResponse>

    @Receive
    fun observeTypingChanged(): Flow<TypingChangedResponse>

    @Send
    fun sendNewMessageText(newMessageRequest: NewMessageRequest): Boolean

    @Send
    fun sendStartTyping(startTypingRequest: StartTypingRequest): Boolean

    @Send
    fun sendEndTyping(endTypingRequest: EndTypingRequest): Boolean
}