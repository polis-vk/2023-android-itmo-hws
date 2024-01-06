package `5task`

import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val arr = Array<Int>(10000000) {i -> (0..1000).random()}
    val time = measureTimeMillis {
        val firstSum = SumArrRun(arr.sliceArray(0 until arr.size / 2))
        val secondSum = SumArrRun(arr.sliceArray(arr.size / 2 until arr.size))
        val threadA = Thread(firstSum)
        val threadB = Thread(secondSum)
        threadA.start()
        threadB.start()
        threadA.join()
        threadB.join()
        println(firstSum.sum + secondSum.sum)
    }
    println("Time in ms with 2 threads: $time")
}

class SumArrRun(private val arr: Array<Int>) : Runnable {
    var sum = 0;
    override fun run() {
        sum = arr.sum()
    }
}