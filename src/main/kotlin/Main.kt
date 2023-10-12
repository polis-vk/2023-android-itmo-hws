import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main() {
    fifth()
}
fun first() {
    val array = arrayOf(1, 2, 3, 4, 5)
    Thread(ArraySum(array)).start()
}
private class ArraySum(private val array: Array<Int>) : Runnable {
    override fun run() {
        println(array.sum())
    }
}
fun second() {
    val service:ExecutorService = Executors.newFixedThreadPool(2)
    val result = service.submit(Callable {
        Pow(2,3).call()
    })
    println(result.get())
    service.shutdown()
}
private class Pow(private val number: Int, private val pow: Int) : Callable<Int> {
    private fun pow(number: Int, pow: Int) : Int {
        if (pow == 0) {
            return 1
        }
        return pow(number, pow - 1) * number
    }
    override fun call(): Int {
        return pow(number, pow)
    }
}
fun third() {
    val thread1 = Thread1()
    val thread2 = Thread2()
    Thread(thread1).start()
    Thread(thread2).start()
    //Deadlock
}
private val obj1 = Object()
private val obj2 = Object()
private class Thread1 : Runnable {
    override fun run() {
        synchronized(obj1) {
            Thread.sleep(10L);
            synchronized(obj2) {
                println("obj2 blocks obj1")
            }
        }
    }
}
private class Thread2 : Runnable {
    override fun run() {
        synchronized(obj2) {
            Thread.sleep(10L);
            synchronized(obj1) {
                println("obj1 blocks obj2")
            }
        }
    }
}
private val obj_synch = Object()
fun fourth() {
    var counterWithSync = 0
    var counterWithOutSync = 0
    fun incWithSync() {
        synchronized(obj_synch) {
            counterWithSync++
        }
    }
    fun incWithOutSync() {
        counterWithOutSync++
    }
    val thread1 = Thread {
        for (i in 1..1000) {
            incWithSync()
            incWithOutSync()
        }
    }
    val thread2 = Thread {
        for (i in 1..1000) {
            incWithSync()
            incWithOutSync()
        }
    }
    val thread3 = Thread {
        for (i in 1..1000) {
            incWithSync()
            incWithOutSync()
        }
    }
    thread1.start()
    thread2.start()
    thread3.start()
    thread1.join()
    thread2.join()
    thread3.join()
    println("counterWithSync: $counterWithSync")
    println("counterWithOutSync: $counterWithOutSync")
}
fun fifth() {
    val array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val firstHalf = array.copyOfRange(0, (array.size + 1) / 2)
    val secondHalf = array.copyOfRange((array.size + 1) / 2, array.size)
    val sum1 = ArraySumDivide(firstHalf)
    val sum2 = ArraySumDivide(secondHalf)
    val thread1 = Thread(sum1)
    val thread2 = Thread(sum2)
    thread1.start()
    thread2.start()
    thread1.join()
    thread2.join()
    println(sum1.getsum() + sum2.getsum())
}
private class ArraySumDivide(private val array : Array<Int>) : Runnable {
    var sum: Int = 0
    fun getsum() : Int {
        return sum
    }
    override fun run() {
        sum = array.sum()
    }
}
