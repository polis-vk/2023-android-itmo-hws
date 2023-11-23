package ru.ok.itmo.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import ru.ok.itmo.example.login.LoginApi
import ru.ok.itmo.example.login.LoginRequest
import ru.ok.itmo.example.login.RetrofitLogin

class Autorize : Fragment(R.layout.autorize) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login = view.findViewById<EditText>(R.id.login_text)
        val password = view.findViewById<EditText>(R.id.password_text)
        val enter = view.findViewById<Button>(R.id.enter_button)
        login.addTextChangedListener {
            enter.isEnabled = !(login.text.isEmpty() || password.text.isEmpty())
        }
        password.addTextChangedListener {
            enter.isEnabled = !(login.text.isEmpty() || password.text.isEmpty())
        }
        val loginApi = LoginApi.create(RetrofitLogin().login())
        enter.setOnClickListener {
            val loginRequest = LoginRequest(loginApi,
                login.text.toString(), password.text.toString())
            loginRequest.login()
            when (loginRequest.errorCode) {
                200 -> {
                    val noname = NoName(loginRequest)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, noname).addToBackStack(null).commit()
                }
                401 -> {
                    Log.d(401.toString(), "you are not authorised")
                }
                408 -> {
                    Log.d(408.toString(), "bad internet connection")
                }
                else -> {
                    Log.d(403.toString(), "we dont know what error")
                }
            }
            login.text.clear()
            password.text.clear()
        }
    }
}
