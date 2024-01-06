package `3task`

import java.util.concurrent.locks.ReentrantLock


fun main(args: Array<String>) {
    // Это, наверное, единственное, что я придумал без sleep(), но у меня точно нет идей
    val a = ReentrantLock()
    val b = ReentrantLock()

    val thread1 = Thread {
        a.lock()
        b.lock()
        println("Я тоже всё захватил")
        a.unlock()
        b.unlock()
    }
    val thread2 = Thread {
        b.lock()
        a.lock()
        println("Я всё захватил")
        thread1.start()
        thread1.join()
        b.unlock()
        a.unlock()
    }

    thread2.start()
}