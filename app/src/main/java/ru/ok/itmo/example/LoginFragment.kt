package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.example.data.LoginData
import ru.ok.itmo.example.login.LoginState
import ru.ok.itmo.example.login.LoginViewModel

class LoginFragment : Fragment(R.layout.login_fragment) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = view.findViewById<EditText>(R.id.password)
        val login = view.findViewById<EditText>(R.id.login)
        val button = view.findViewById<Button>(R.id.login_enter)
        password.addTextChangedListener {
            button.isEnabled = !(password.text.toString() == "" || login.text.toString() == "")
        }
        login.addTextChangedListener {
            button.isEnabled = !(password.text.toString() == "" || login.text.toString() == "")
        }
        button.setOnClickListener {
            val passwordText: String  = password.text.toString()
            val loginText: String  = login.text.toString()
            if (correctCheck(loginText, passwordText)) {
                viewModel.login(LoginData(loginText, passwordText))
            } else {
                Toast.makeText(requireActivity(), "Incorrect password or login", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.state.onEach {
                if (it is LoginState.Success) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MainFragment())
                        .commit()
                } else if (it is LoginState.Error) {
                    Toast.makeText(requireActivity(), it.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }.stateIn(this)
        }
    }

    private fun correctCheck(login: String, password: String): Boolean {
        if (login.isEmpty()) {
            return false
        } else if (password.isEmpty()) {
            return false
        } else if (!loginIsCorrect(login)) {
            return false
        } else return passwordIsCorrect(password)
    }

    private fun loginIsCorrect(login: String): Boolean {
        return login.length >= 3
    }

    private fun passwordIsCorrect(password: String): Boolean {
        return password.length > 6 && password.lowercase() != password
                && password.uppercase() != password
    }
}