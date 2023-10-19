package ru.ok.itmo.example

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var timer: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val progressbarButton: ProgressBar = findViewById(R.id.progressbar_button)
        val clickButton: Button = findViewById(R.id.click_me)
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)
        val resetButton: Button = findViewById(R.id.reset_me)
        var rxJava: Disposable? = null
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.radio_button50 -> timer = 50
                R.id.radio_button100 -> timer = 100
                R.id.radio_button300 -> timer = 300
                R.id.radio_button500 -> timer = 500
            }
        }
        var progressMaking = false
        fun threadRunning() {
            val thread1 = Thread {
                while (progressMaking) {
                    if (progressbarButton.progress >= 99)
                    {
                        progressMaking = false
                    }
                    runOnUiThread {
                        progressbarButton.progress++
                    }
                    Thread.sleep(timer)
                }
            }
            thread1.start()
        }
        fun rxJavaRunning() {
            rxJava = Observable.interval(timer, TimeUnit.MILLISECONDS).
            takeWhile{progressMaking}.subscribeOn(Schedulers.newThread()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (progressbarButton.progress >= 99)
                {
                    progressMaking = false
                }
                runOnUiThread {
                    progressbarButton.progress++
                }
            }
        }
        clickButton.setOnClickListener {
            progressMaking = true
            rxJavaRunning()
        }
        resetButton.setOnClickListener {
            progressbarButton.progress = 0
            progressMaking = false
            rxJava = null
        }
    }
}
