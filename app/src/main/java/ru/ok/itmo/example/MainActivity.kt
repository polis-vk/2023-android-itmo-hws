package ru.ok.itmo.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.ok.itmo.example.auth.EnterFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .apply {
                when (savedInstanceState) {
                    null -> add(R.id.screen_container, EnterFragment(), "enter")
                    else -> show(supportFragmentManager.findFragmentByTag("enter")!!)
                }
            }
            .commit()
    }
}
