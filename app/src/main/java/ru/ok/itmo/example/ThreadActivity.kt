package ru.ok.itmo.example

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ThreadActivity : AppCompatActivity(R.layout.activity_action) {

    private lateinit var buttonStart: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var testTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonStart = findViewById(R.id.startButton)
        progressBar = findViewById(R.id.progress_bar)
        testTextView = findViewById(R.id.test_tv)
        var isConfigRestarted = true

        buttonStart.setOnClickListener {
            if (isProgressEmpty() || isProgressFull() || isConfigRestarted) {
                getProcessThread().start()
                isConfigRestarted = false
            }
        }
    }


    private fun getProcessThread(): Thread {
        return Thread {
            for (i in 0..100) {
                runOnUiThread {
                    progressBar.progress = i
                    testTextView.text = i.toString()
                }
                Thread.sleep(100)
            }
        }
    }


    private fun isProgressEmpty(): Boolean {
        return progressBar.progress == 0
    }

    private fun isProgressFull(): Boolean {
        return progressBar.progress == 100
    }


}