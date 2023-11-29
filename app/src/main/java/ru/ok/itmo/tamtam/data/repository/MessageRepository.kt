package ru.ok.itmo.tamtam.data.repository

import android.content.Context
import androidx.core.math.MathUtils
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.data.AccountStorage
import ru.ok.itmo.tamtam.data.retrofit.MessageService
import ru.ok.itmo.tamtam.data.retrofit.model.MessageDto
import ru.ok.itmo.tamtam.data.retrofit.model.TextDataDto
import ru.ok.itmo.tamtam.data.retrofit.model.TextDto
import ru.ok.itmo.tamtam.data.retrofit.model.TextMessageDto
import ru.ok.itmo.tamtam.data.room.dao.MessageDao
import ru.ok.itmo.tamtam.data.scarlet.api.MessageApi
import ru.ok.itmo.tamtam.data.scarlet.model.ChatNameGPart
import ru.ok.itmo.tamtam.data.scarlet.model.EndTypingRequest
import ru.ok.itmo.tamtam.data.scarlet.model.NewMessageRequest
import ru.ok.itmo.tamtam.data.scarlet.model.StartTypingRequest
import ru.ok.itmo.tamtam.data.scarlet.model.TextMessageGPart
import ru.ok.itmo.tamtam.domain.model.Chat
import ru.ok.itmo.tamtam.domain.model.Contact
import ru.ok.itmo.tamtam.domain.model.Message
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.utils.ApplicationContext
import ru.ok.itmo.tamtam.utils.NotificationType
import ru.ok.itmo.tamtam.utils.Resource
import ru.ok.itmo.tamtam.utils.handleResult
import ru.ok.itmo.tamtam.utils.merge
import javax.inject.Inject

@AppComponentScope
class MessageRepository @Inject constructor(
    private val messageService: MessageService,
    private val messageDao: MessageDao,
    private val accountStorage: AccountStorage,
    @ApplicationContext private val context: Context
) {
    val notifications: Channel<NotificationType> = Channel()

    private val _typing = MutableStateFlow(mapOf<String, List<String>>())
    private val typing: StateFlow<Map<String, List<String>>> get() = _typing

    private var messageApi: MessageApi? = null

    private val _chats = MutableStateFlow(emptyList<Chat>())
    val chats: Flow<List<Chat>>
        get() = _chats.map {
            it.map { chat ->
                chat.copy(
                    lastMessage = messageDao.getMessageById(chat.lastMessageId),
                    typingUsers = typing.value[chat.name].orEmpty(),
                    countNewMessage = MathUtils.clamp(
                        messageDao
                            .getCountMessageBetweenIdsForChat(
                                chatName = chat.name,
                                startId = chat.lastViewedMessageId,
                                endId = chat.lastMessageId
                            ) - 1, 0, Int.MAX_VALUE
                    )
                )
            }
        }
            .flowOn(Dispatchers.IO)

    private var trackingJob: Job? = null

    suspend fun startTracking() {
        messageApi = (context as App).appComponent.getMessageApi()
        trackingJob = coroutineScope {
            launch {
                typing.collect {

                    _chats.emit(_chats.value.map {
                        it.copy(typingUsers = listOf(System.currentTimeMillis().toString()))
                    })
                }
            }
            launch {
                messageDao.getChatsAsFlow()
                    .collect {
                        _chats.emit(it)
                    }
            }
            launch {
                messageApi?.observeTypingChanged()
                    ?.mapNotNull { it.typingChangedGPart?.newTypingGPart }
                    ?.collect {
                        _typing.emit(it)
                    }
            }
            launch {
                messageApi?.observeEvents()?.collect {
                    when (it) {
                        is WebSocket.Event.OnConnectionOpened<*> -> {
                            val resource = synchronize()
                            if (resource is Resource.Failure) {
                                val notificationType = resource.throwable as? NotificationType
                                notificationType?.let { notifications.trySend(it) }
                            }
                        }

                        is WebSocket.Event.OnMessageReceived -> {
                        }

                        is WebSocket.Event.OnConnectionClosing -> {
                        }

                        is WebSocket.Event.OnConnectionClosed -> {
                        }

                        is WebSocket.Event.OnConnectionFailed -> {
                            notifications.trySend(NotificationType.Connection(RuntimeException("Connection Failed")))
                        }
                    }
                }
            }
            launch {
                messageApi?.observeNewMessage()
                    ?.mapNotNull { it.newMessageGPart?.messageGPart }
                    ?.collect {
                        messageDao.addMessages(
                            listOf(
                                Message(
                                    id = it.id,
                                    chatName = "",
                                    from = it.from,
                                    to = it.to,
                                    time = it.time,
                                    messageText = it.dataGPart.textGPart?.text,
                                    imageLink = it.dataGPart.imageGPart?.link,
                                    isSent = true
                                )
                            ),
                            accountStorage.login!!
                        )
                    }
            }
        }
    }

    fun stopTracking() {
        messageApi = null
        trackingJob?.cancel()
    }

    private var synchronizeMutex: Mutex = Mutex(false)

    private suspend fun synchronize(): Resource<Unit> {
        val login = accountStorage.login!!
        if (synchronizeMutex.isLocked) return Resource.Success(Unit)
        synchronizeMutex.withLock {
            val messageListFromChannels = runCatching {
                messageService.channels()
            }.handleResult()
                .mapIfSuccess { channels ->
                    val resultList = mutableListOf<List<MessageDto>>()
                    for (channelName in channels) {
                        runCatching {
                            messageService.listOfMessagesFromChannel(
                                channelName = channelName,
                                queryMap = mapOf(
                                    "limit" to "1",
                                    "lastKnownId" to "${Int.MAX_VALUE}",
                                    "reverse" to "true"
                                )
                            )
                        }.handleResult()
                            .getOrNull()
                            ?.also { resultList.add(it) }
                    }
                    return@mapIfSuccess resultList.flatten()
                }
            val lastKnownId = messageDao.getMaxLastMessageIdForNotChannel() ?: 0
            val messageListForUser = runCatching {
                messageService.listOfMessagesForUser(
                    userName = login,
                    queryMap = mapOf(
                        "limit" to "${Int.MAX_VALUE}",
                        "lastKnownId" to "$lastKnownId",
                        "reverse" to "false"
                    )
                )
            }.handleResult()
            val messagesResource = messageListForUser.merge(messageListFromChannels)
            val messages = messagesResource.getOrNull().orEmpty()
            messageDao.addMessages(messages.map {
                Message(
                    id = it.id,
                    chatName = "",
                    from = it.from,
                    to = it.to,
                    time = it.time,
                    messageText = it.data.text?.text,
                    imageLink = it.data.image?.link,
                    isSent = true
                )
            }, login)
            return messagesResource.mapIfSuccess {}
        }
    }

    suspend fun sendMessage(message: String, chat: Chat) {
        withContext(Dispatchers.IO) {
            if (chat.isChannel) {
                sendNewMessageText(chat.name, message)
            } else {
                val textMessageDto = TextMessageDto(
                    id = 1,
                    from = accountStorage.login!!,
                    to = chat.name,
                    time = System.currentTimeMillis(),
                    data = TextDataDto(
                        text = TextDto(
                            text = message
                        )
                    )
                )
                val newIdResource = runCatching {
                    messageService.sendTextMessage(textMessageDto)
                }.handleResult()
                    .getOrNull()
                    ?: run {
                        notifications.send(NotificationType.Unknown(RuntimeException("cant send message"))); return@withContext
                    }
                val sentMessage = Message(
                    id = newIdResource.toInt(),
                    chatName = chat.name,
                    from = accountStorage.login!!,
                    to = chat.name,
                    time = System.currentTimeMillis(),
                    messageText = message,
                    imageLink = null,
                    isSent = false
                )
                messageDao.addMessages(
                    iMessages = listOf(sentMessage),
                    login = accountStorage.login!!,
                    isSent = false
                )
            }
        }
    }


    private suspend fun sendNewMessageText(to: String, message: String) {
        withContext(Dispatchers.IO) {
            messageApi?.sendNewMessageText(
                NewMessageRequest(
                    TextMessageGPart(
                        to = to,
                        text = message
                    )
                )
            )
        }
    }

    suspend fun sendStartTyping(chatName: String) {
        withContext(Dispatchers.IO) {
            messageApi?.sendStartTyping(StartTypingRequest(ChatNameGPart(chatName)))
        }
    }

    suspend fun sendEndTyping() {
        withContext(Dispatchers.IO) {
            messageApi?.sendEndTyping(EndTypingRequest(emptyMap()))
        }
    }

    suspend fun getChatByName(chatName: String): Resource<Chat> {
        val chat = messageDao.getChatByName(chatName) ?: Chat(
            name = chatName,
            isAttach = false,
            lastViewedMessageId = 0,
            lastMessageId = 0,
            lastMessage = null,
            isChannel = false,
            typingUsers = emptyList(),
            countNewMessage = 0
        )
        return Resource.Success(chat)
    }

    suspend fun getMessages(
        chatName: String,
        lastKnownId: Int,
        count: Int,
        isAfter: Boolean,
        isLocal: Boolean
    ): Resource<List<Message>> {

        if (isLocal) {
            val localMessages = if (isAfter) {
                messageDao.getMessagesAfter(
                    chatName = chatName,
                    lastKnownId = lastKnownId,
                    count = count,
                )
            } else {
                messageDao.getMessagesBefore(
                    chatName = chatName,
                    lastKnownId = lastKnownId,
                    count = count,
                )
            }
            return Resource.Success(localMessages)
        } else {
            val channelName = messageDao.getChatByName(chatName)!!.name
            val remoteMessages = if (isAfter) {
                runCatching {
                    messageService.listOfMessagesFromChannel(
                        channelName = channelName,
                        queryMap = mapOf(
                            "limit" to "$count",
                            "lastKnownId" to "${lastKnownId}",
                            "reverse" to "false"
                        )
                    )
                }.handleResult()
                    .mapIfSuccess {
                        it.map {
                            Message(
                                id = it.id,
                                chatName = chatName,
                                from = it.from,
                                to = it.to,
                                time = it.time,
                                messageText = it.data.text?.text,
                                imageLink = it.data.image?.link,
                                isSent = true
                            )
                        }
                    }
                    .getOrNull().orEmpty()
            } else {
                runCatching {
                    messageService.listOfMessagesFromChannel(
                        channelName = channelName,
                        queryMap = mapOf(
                            "limit" to "$count",
                            "lastKnownId" to "${lastKnownId + 1}",
                            "reverse" to "true"
                        )
                    )
                }.handleResult()
                    .mapIfSuccess {
                        it.map {
                            Message(
                                id = it.id,
                                chatName = chatName,
                                from = it.from,
                                to = it.to,
                                time = it.time,
                                messageText = it.data.text?.text,
                                imageLink = it.data.image?.link,
                                isSent = true
                            )
                        }
                    }
                    .getOrNull().orEmpty()
            }
            return Resource.Success(remoteMessages)
        }
    }

    suspend fun updateLastViewedForChat(chatId: String, lastViewedMessageId: Int) {
        withContext(Dispatchers.IO) {
            messageDao.updateLastViewedForChat(chatId, lastViewedMessageId)
        }
    }

    suspend fun getContacts(): Resource<List<Contact>> {
        return runCatching {
            messageService.users()
        }.handleResult().mapIfSuccess { it.map { Contact(name = it) } }
    }
}