package ru.ok.itmo.example

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxActivity : AppCompatActivity(R.layout.activity_action) {

    private lateinit var buttonStart: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var testTextView: TextView
    private lateinit var disposable: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonStart = findViewById(R.id.startButton)
        progressBar = findViewById(R.id.progress_bar)
        testTextView = findViewById(R.id.test_tv)
        var isConfigRestarted = true

        buttonStart.setOnClickListener {
            if (isProgressEmpty() || isProgressFull() || isConfigRestarted) {
                isConfigRestarted = false
                disposable = Observable.intervalRange(
                    1, 100, 0, 100, TimeUnit.MILLISECONDS
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        progressBar.progress = it.toInt()
                        testTextView.text = it.toString()
                    }
            }
        }
    }

    override fun onDestroy() {
        if (::disposable.isInitialized && !disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }


    private fun isProgressEmpty(): Boolean {
        return progressBar.progress == 0
    }

    private fun isProgressFull(): Boolean {
        return progressBar.progress == 100
    }

}