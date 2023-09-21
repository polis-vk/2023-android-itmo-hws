package company.vk.polis.task1

sealed class State {
    class Read: State()
    class Unread: State()
    class Deleted(val userId: Int): State()

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is State) return false
        return when (other) {
            is Read -> this is Read
            is Unread -> this is Unread
            is Deleted -> this is Deleted
        }
    }
}