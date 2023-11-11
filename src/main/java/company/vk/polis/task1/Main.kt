package company.vk.polis.task1

fun main() {
    val messageController = MessageController()
    println(messageController.getChatItems(1, StateEnum.DELETED))
    println(messageController.countMessages(1))
}