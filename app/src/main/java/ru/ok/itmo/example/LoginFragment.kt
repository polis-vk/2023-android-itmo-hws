package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.example.login.LoginState
import ru.ok.itmo.example.login.LoginViewModel

class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = view.findViewById<EditText>(R.id.password)
        val login = view.findViewById<EditText>(R.id.login)
        val button = view.findViewById<Button>(R.id.login_enter)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        password.addTextChangedListener {
            button.isEnabled = !(password.text.toString() == "" || login.text.toString() == "")
        }
        login.addTextChangedListener {
            button.isEnabled = !(password.text.toString() == "" || login.text.toString() == "")
        }
        button.setOnClickListener {
            val passwordText: String  = password.text.toString()
            val loginText: String  = login.text.toString()
            viewModel.loginCheck(loginText, passwordText)
        }

        lifecycleScope.launch {
            viewModel.state.onEach {
                if (it is LoginState.Success) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MainFragment())
                        .commit()
                } else if (it is LoginState.Error) {
                    Toast.makeText(requireActivity(), it.error.toString(), Toast.LENGTH_SHORT).show()
                } else if (it is LoginState.Incorrect) {
                    Toast.makeText(requireActivity(), "Incorrect password or login", Toast.LENGTH_SHORT).show()
                }
            }.stateIn(this)
        }
    }
}