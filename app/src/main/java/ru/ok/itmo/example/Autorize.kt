package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Autorize : Fragment(R.layout.autorize) {
    val noname = NoName()
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
        val chatApi = ChatApi.create(RetrofitLogin().login())
        enter.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val token = chatApi.login(
                        Login(
                            login.text.toString(),
                            password.text.toString()
                        )
                )
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, noname).addToBackStack(null).commit()
        }
    }
}
