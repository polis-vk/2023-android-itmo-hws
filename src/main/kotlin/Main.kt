import java.lang.Runnable
import java.lang.Thread
import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock

fun main() {
    println("task1 2point")

    class SumCalc(private val numbers: IntArray) : Runnable {
        override fun run() {
            val sum = numbers.sum()
            println(sum)
        }
    }

    val calculator = SumCalc(intArrayOf(1, 2, 3, 4, 5))

    val thread = Thread(calculator)
    thread.start()
    thread.join()


    println("task2 2point")

    class PowCalc(private val base: Double, private val exponent: Int) : Callable<Double> {
        override fun call(): Double = Math.pow(base, exponent.toDouble())
    }

    val base = 4.0
    val exp = 3

    val executorService = Executors.newSingleThreadExecutor()

    val future: Future<Double> = executorService.submit(PowCalc(base, exp))
    val result = future.get()
    println(result)
    executorService.shutdown()

    println("task3 3point")

    val lock1 = ReentrantLock()
    val lock2 = ReentrantLock()

    val locker1 = Thread {
        lock1.lock()
        lock2.lock()
    }

    val locker2 = Thread {
        lock2.lock()
        lock1.lock()
    }

//    locker1.start()
//    locker2.start()
//
//    locker1.join()
//    locker2.join()

    println("task4 3point")

    var counter = 0
    var uncounter = 0
    val range = 3
    val numIterations = 0..999
    val syncObject = Any()

    val Without = List(range) {
        Thread {
            for (j in numIterations) {
                counter++
            }
        }
    }
    Without.forEach { it.start() }
    Without.forEach { it.join() }
    println("Без синхронизации:$counter")
    val With = List(range) {
        Thread {
            for (j in numIterations) {
                synchronized(syncObject) {
                    uncounter++
                }
            }
        }
    }
    With.forEach { it.start() }
    With.forEach { it.join() }
    println("С синхронизацией:$uncounter")
}
