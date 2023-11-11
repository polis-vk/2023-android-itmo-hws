package company.vk.polis.task1

data class State(private val state: StateEnum) {
    private var deletedBy: Long? = null

    constructor(state: StateEnum, deletedBy: Long?) : this(state) {
        if (deletedBy != null && state != StateEnum.DELETED) {
            throw IllegalArgumentException("State can't be with deleted id")
        }
        this.deletedBy = deletedBy
    }

    override fun toString(): String {
        return state.toString()
    }

    fun getDeletedBy(): Long? = deletedBy
    fun getState(): StateEnum = state
}

enum class StateEnum {
    READ, UNREAD, DELETED
}