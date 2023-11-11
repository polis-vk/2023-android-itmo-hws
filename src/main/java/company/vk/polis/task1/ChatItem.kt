package company.vk.polis.task1

internal data class ChatItem(
    val avatarUrl: String?,
    val lastMessage: Message,
    val lastMessageAuthor: User,
    val state: State
) {
    override fun toString(): String {
        if (state.getState() == StateEnum.DELETED) {
            return "Сообщение было удалено ${lastMessageAuthor.name}"
        }
        return "Avatar=${avatarUrl}\nLastMessage={${lastMessage.text}}\nState=${state}"
    }
}