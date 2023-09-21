package company.vk.polis.task1

fun main() {
    val messageController = MessageController()
    val userId = 0
    val items = messageController.getChatItems(userId)
    val countOfUnreadMessages = messageController.getNubmerOfMessages(userId)

    println("Count of unread messages: $countOfUnreadMessages\n")

}