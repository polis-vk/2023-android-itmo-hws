package company.vk.polis.task1

internal class MessageController {
    private val entities: List<Entity> = Repository.getInfo().filter { EntityValidator.validate(it) }
    private val chats = entities.filterIsInstance<BaseChat>()
    private val messages = entities.filterIsInstance<Message>()
    private var users: Map<Int, User> = entities.filterIsInstance<User>().associateBy { it.id }

    fun getChatItems(userId: Int, state: StateEnum?): List<ChatItem> {
        return chats.filter { it.getUserIds().contains(userId) }
            .associateBy { it }
            .mapValues { getMessagesByIds(it.value.getMessageIds()) }
            .mapValues { it.value.filter { m -> m.senderId == userId } }
            .filterValues { it.isNotEmpty() }
            .mapValues { it.value.last() }
            .filterValues { state == null || it.state.getState() == state }
            .map { toChatItem(it.key, it.value, userId) }
    }

    fun countMessages(userId: Int): Int {
        return messages.count { it.senderId == userId }
    }

    private fun getMessagesByIds(ids: Collection<Int>): List<Message> {
        return messages.filter { ids.contains(it.id) }
    }

    private fun toChatItem(chat: BaseChat, message: Message, userId: Int): ChatItem {
        val user = getUser(userId) ?: throw IllegalArgumentException("Userid $userId not found")
        return ChatItem(user.avatarUrl, message, user, message.state)
    }

    private fun getUser(userId: Int): User? {
        return users[userId]
    }
}