package ru.ok.itmo.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    private lateinit var buttonThreadBegin : Button
    private lateinit var buttonRxBegin : Button
    private lateinit var buttonAddBegin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonThreadBegin = findViewById(R.id.button_thread_begin)
        buttonRxBegin = findViewById(R.id.button_rx_begin)
        buttonAddBegin = findViewById(R.id.button_additional_begin)

        buttonThreadBegin.setOnClickListener {
            startActivity(Intent(this, ThreadActivity::class.java))
        }

        buttonRxBegin.setOnClickListener {
            startActivity(Intent(this, RxActivity::class.java))
        }
        buttonAddBegin.setOnClickListener {
            startActivity(Intent(this, AdditionalActivity::class.java))
        }

    }
}

