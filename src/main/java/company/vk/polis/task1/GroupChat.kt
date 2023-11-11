package company.vk.polis.task1

data class GroupChat(private val id: Int, private val userIds: List<Int>, private val messageIds: List<Int>,
                private val groupAvatar: String?) : BaseChat {
    override fun getId(): Int = id
    override fun getMessageIds(): List<Int> = messageIds
    override fun getUserIds(): List<Int> = userIds
    override fun getChatAvatar(): String? = groupAvatar
}