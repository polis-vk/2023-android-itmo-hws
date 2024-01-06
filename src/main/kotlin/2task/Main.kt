package `2task`

import java.util.concurrent.Executors
import kotlin.math.pow
import kotlin.random.Random
import java.util.concurrent.Callable

fun main(args: Array<String>) {
    val exec = Executors.newSingleThreadExecutor()
    val a = Random.nextDouble(until = 10.0)
    val b = Random.nextDouble(until = 10.0)
    val future = exec.submit(PowCall(a, b))
    println(a)
    println(b)
    println(future.get())
    exec.shutdown()
}

class PowCall(private val a: Double, private val b : Double) : Callable<Double> {
    override fun call(): Double {
        return a.pow(b)
    }
}