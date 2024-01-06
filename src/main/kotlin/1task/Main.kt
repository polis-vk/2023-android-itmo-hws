package `1task`

fun main(args: Array<String>) {
    val arr = Array<Int>(10) {i -> (0..1000).random()}
    val sumThread = Thread(SumArrRun(arr))
    println(arr.joinToString(separator = " "))
    sumThread.start()
}

class SumArrRun(private val arr: Array<Int>) : Runnable {
    override fun run() {
        println(arr.sum())
    }
}