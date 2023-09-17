package company.vk.polis.task1

data class GroupChat(val id : Int, val avatarUrl : String?, val usersIds : List<Int>, val messageIds : List<Int>) : Entity {
    override fun getId(): Int {
        return id;
    }

    override fun getType(): Int {
        return 3;
    }
}
