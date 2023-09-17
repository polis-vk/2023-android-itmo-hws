package company.vk.polis.task1

fun main() {
    val user1 = User(1, "Peter", "https://abc.d")
    val user2 = User(2, "Paul", "https://abc.e")
    val user3 = User(3, "Andrew", "https://abc.f")
    val chat = Chat(1, UserPair(1, 2), listOf(1, 2))
    val message1 = Message(1, "Abc", 1, 100)
    val message2 = Message(2, "Def", 1, 200)
    val mc = MessageController(listOf(user1, user2, user3, chat, message1, message2).shuffled())
    println(mc.getCountOfMessages(1))
}
