package ru.ok.itmo.example.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import ru.ok.itmo.example.R
import ru.ok.itmo.example.main.HomeFragment
import ru.ok.itmo.example.utils.disable
import ru.ok.itmo.example.utils.enable

class LoginFragment : Fragment(R.layout.login_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.backwards).setOnClickListener { requireActivity().onBackPressed() }

        val login = view.findViewById<TextInputEditText>(R.id.login)
        val password = view.findViewById<TextInputEditText>(R.id.password)
        val submit = view.findViewById<Button>(R.id.login_button)

        fun toast(message: String) = Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (login.text.isNullOrBlank() || password.text.isNullOrBlank()) {
                    submit.disable()
                } else {
                    submit.enable()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }

        login.addTextChangedListener(watcher)
        password.addTextChangedListener(watcher)

        submit.disable()
        submit.setOnClickListener {
            val loginText = login.text?.toString()
            val passwordText = password.text?.toString()

            when {
                loginText.isNullOrBlank() -> toast("No login is provided")
                passwordText.isNullOrBlank() -> toast("No password is provided")
                else -> {
                    parentFragmentManager.beginTransaction()
                        .remove(this)
                        .replace(R.id.screen_container, HomeFragment(), "home")
                        .addToBackStack("home")
                        .commit()
                }
            }
        }

        view.findViewById<TextView>(R.id.forgot_password).setOnClickListener {
            toast("Not supported")
        }
    }
}