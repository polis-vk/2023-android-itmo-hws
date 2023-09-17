package company.vk.polis.task1

import java.util.LinkedList

class MessageController(entities: List<Entity>) {
    private var items = LinkedHashMap<Int, LinkedHashMap<Int, ChatItem>>()
    private var names = LinkedHashMap<Int, String>()
    private var count = LinkedHashMap<Int, Int>()
    private var fromUserToUrl = LinkedHashMap<Int, String?>()
    private var fromChatToMessageId = LinkedHashMap<Int, List<Int>>()
    private var fromUserToChats = LinkedHashMap<Int, LinkedHashSet<Int>>()
    private var fromMessageIdToMessage = LinkedHashMap<Int, Message>()
    private var fromGroupChatToUrl = LinkedHashMap<Int, String?>()
    private var fromGroupChatToMessageId = LinkedHashMap<Int, List<Int>>()
    private var fromUserToGroupChats = LinkedHashMap<Int, LinkedHashSet<Int>>()
    init {
        for (i in entities) {
            when (i.type) {
                0 -> {
                    fromChatToMessageId[(i as Chat).id] = i.messageIds
                    fromUserToChats[i.userIds.senderId]?.add(i.id)
                    fromUserToChats[i.userIds.receiverId]?.add(i.id)
                }
                1 -> {
                    fromMessageIdToMessage[(i as Message).id] = i
                }
                2 -> {
                    names[(i as User).id] = i.name
                    fromUserToUrl[i.id] = i.avatarUrl
                }
                3 -> {
                    fromGroupChatToMessageId[(i as GroupChat).id] = i.messageIds
                    for (j in i.usersIds) {
                        fromUserToGroupChats[j]?.add(i.id)
                    }
                    fromGroupChatToUrl[i.id] = i.avatarUrl
                }
            }
        }
        for (i in fromGroupChatToMessageId.keys) {
            for (j in fromGroupChatToMessageId[i]!!) {
                val message = fromMessageIdToMessage[j]
                if (message != null) {
                    if (count[message.senderId] == null) {
                        count[message.senderId] = 1
                    } else {
                        count[message.senderId] = count[message.senderId]!! + 1
                    }
                    if (items[message.senderId]?.get(i) == null) {
                        items[message.senderId]?.put(i, ChatItem(fromUserToUrl[message.senderId], message, message.state))
                        if (message.state == State.DELETED) {
                            println("Сообщение было удалено ${names[message.senderId]}")
                        }
                    } else {
                        if (items[message.senderId]!![i]?.lastMessage?.timestamp!! < message.timestamp) {
                            items[message.senderId]!![i] = ChatItem(fromUserToUrl[message.senderId], message, message.state)
                            if (message.state == State.DELETED) {
                                println("Сообщение было удалено ${names[message.senderId]}")
                            }
                        }
                    }
                }
            }
        }
        for (i in fromChatToMessageId.keys) {
            for (j in fromChatToMessageId[i]!!) {
                val message = fromMessageIdToMessage[j]
                if (message != null) {
                    if (count[message.senderId] == null) {
                        count[message.senderId] = 1
                    } else {
                        count[message.senderId] = count[message.senderId]!! + 1
                    }
                    if (items[message.senderId]?.get(i) == null) {
                        items[message.senderId]?.put(i, ChatItem(fromUserToUrl[message.senderId], message, message.state))
                        if (message.state == State.DELETED) {
                            println("Сообщение было удалено ${names[message.senderId]}")
                        }
                    } else {
                        if (items[message.senderId]!![i]?.lastMessage?.timestamp!! < message.timestamp) {
                            items[message.senderId]!![i] = ChatItem(fromUserToUrl[message.senderId], message, message.state)
                            if (message.state == State.DELETED) {
                                println("Сообщение было удалено ${names[message.senderId]}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun filterUser(id: Int, state : State?) : LinkedList<ChatItem> {
        val result = LinkedList<ChatItem>()
        for (item in items[id]?.values!!) {
            if (state == null || item.state == state) {
                result.add(item)
            }
        }
        return result
    }

    fun getCountOfMessages(id : Int) = count[id]
}
