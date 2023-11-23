package ru.ok.itmo.tamtam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.Router.PopRootControllerMode
import com.bluelinelabs.conductor.RouterTransaction
import ru.ok.itmo.tamtam.presentation.StartController

class MainActivity : AppCompatActivity() {
    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container = findViewById<ChangeHandlerFrameLayout>(R.id.container)

        router = Conductor.attachRouter(this, container, savedInstanceState)
            .setPopRootControllerMode(PopRootControllerMode.NEVER)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(StartController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}