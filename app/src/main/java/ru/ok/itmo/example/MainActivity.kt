package ru.ok.itmo.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var buttonStart: Button
    private lateinit var textCount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var radioGroup: RadioGroup
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonStart = findViewById(R.id.button_start)
        textCount = findViewById(R.id.text_count)
        progressBar = findViewById(R.id.progressBar)
        radioGroup = findViewById(R.id.radio_button_count)

        buttonStart.setOnClickListener {
            buttonStart.isEnabled = false
            withObserver()
        }
    }

    fun getCount(): Long {
        val selectedOption: Int = radioGroup.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedOption)
        return (radioButton.text as String).toLong()
    }

    fun withThread() {
        val countLoad = getCount()
        Thread {
                for (n in 1..countLoad) {
                    runOnUiThread {
                        progressBar.progress = (n * 100 / countLoad).toInt()
                        textCount.text = "$n"
                    }
                    Thread.sleep(100)
                }
                runOnUiThread {
                    buttonStart.isEnabled = true
                }
            }.start()
    }

    @SuppressLint("CheckResult")
    fun withObserver() {
        val countLoad = getCount()
        Observable.interval(countLoad, TimeUnit.MILLISECONDS)
            .takeWhile { progressBar.progress != 100 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Long> {
                    override fun onNext(long: Long) {
                        progressBar.incrementProgressBy(1)
                        textCount.text = "${progressBar.progress}"
                    }

                    override fun onSubscribe(d: Disposable) {
                        progressBar.progress = 0
                    }

                    override fun onError(e: Throwable) {
                        System.err.println("Error: " + e.stackTrace)
                    }

                    override fun onComplete() {
                        buttonStart.isEnabled = true
                    }
                }
            )
    }
}