package company.vk.polis.task1

interface BaseChat : Entity {
    fun getMessageIds(): List<Int>
    fun getUserIds(): List<Int>

    fun getChatAvatar(): String?
}