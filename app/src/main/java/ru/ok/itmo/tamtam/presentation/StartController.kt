package ru.ok.itmo.tamtam.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.presentation.loginScreen.LoginAuthController

class StartController : Controller() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        Log.d("StartController", "onCreateView")
        val view = inflater.inflate(R.layout.start_screen, container, false)
        val button = view.findViewById<TextView>(R.id.start_with_login)

        button.setOnClickListener {
            router.pushController(
                RouterTransaction.with(LoginAuthController())
            )
        }
        return view
    }
}