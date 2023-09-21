package company.vk.polis.task1

import java.rmi.UnexpectedException
import java.util.function.Function
import java.util.stream.Collectors

class MessageController {

    private val entities: List<Entity> = Repository.getInfo().filter{ e -> entityValidation(e) }
    private val usersId: Map<Int, User> = getUsersId(entities)
    private val messagesId: Map<Int, Message> = getMessagesId(entities)

    companion object{
        //Entity filter
        private fun entityValidation (entity: Entity): Boolean {
            return when (entity) {
                is User -> entity.name != null && entity.id() != null
                is Chat -> entity.userIds != null && entity.id() != null
                is Message -> entity.text != null && entity.senderId != null && entity.id() != null && entity.state != null
                else -> throw UnexpectedException("Error: unexpected entity type: ${entity.javaClass}")
            }
        }
    }
    //2.1 ChatItem
    data class ChatItem(
        val avatarUrl: String?,
        val messageSentBy: String,
        val lastMessage: String,
        val messageState: String){
        override fun toString(): String {
            return  "Avatar url: $avatarUrl \n" +
                    "Message sent by: $messageSentBy" +
                    "Last message: $lastMessage\n" +
                    "Message state: $messageState"
        }
    }
    //2.2 Method for ChatItem
    fun getChatItems(userId: Int, state: State? = null) : List <ChatItem>{
        val chats: List<Chat> = getChatsForUser(userId)
        val user = usersId[userId]
        val items: MutableList<ChatItem> = ArrayList()
        for (chat in chats){
            val chatItem = chat.messageIds()
                .map { id -> messagesId[id]!! }.toList()
                .filter { message -> message.senderId == user!!.id && (state == null || message.state == state) }
                .stream().min(Comparator.comparingLong { m -> m!!.timestamp })
                .map { message -> ChatItem(user!!.avatarUrl, user.id.toString(), message!!.text, message.state.toString()) }
            if (chatItem.isPresent){
                items.add(chatItem.get())
            }
        }
        return items
    }
    //2.3 Number of messages
    fun getNubmerOfMessages(userId: Int): Int{
        val chats = getChatsForUser(userId)
        val messageIds: MutableSet<Int> = HashSet()
        for (chat in chats){
            messageIds.addAll(chat.messageIds)
        }
        return messagesId
            .map{it.value}
            .filter { messageIds.contains(it.id) && it.state is State.Unread }
            .size
    }
    private fun getChatsForUser(userId: Int): List<Chat> {
        return entities.filterIsInstance<Chat>()
            .filter { c -> c.senderIds().contains(userId) }.toList()
    }
    private fun getUsersId(entities: List<Entity>): Map<Int, User> {
        return entities.filterIsInstance<User>()
            .stream()
            .collect(Collectors
                .toMap(User::id, Function.identity()))
    }
    private fun getMessagesId(entities: List<Entity>): Map<Int, Message>{
        return entities.filterIsInstance<Message>()
            .stream()
            .collect(Collectors
                .toMap(Message::getId, Function.identity()))
    }

}