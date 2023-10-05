package ru.ok.itmo.example

import android.content.res.Configuration
import android.opengl.Visibility

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeSwitch: Switch = findViewById(R.id.themeSwitch)
        val loginField: EditText = findViewById(R.id.loginField)
        val passwordField: EditText = findViewById(R.id.passwordField)
        val showPasswordCheckBox: CheckBox = findViewById(R.id.showPasswordCheckBox)
        val verifyButton: Button = findViewById(R.id.verifyButton)
        val loginErrorTextView: TextView = findViewById(R.id.loginErrorTextView)
        val passwordErrorTextView: TextView = findViewById(R.id.passwordErrorTextView)

        themeSwitch.isChecked = (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            passwordField.inputType = if (isChecked) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        fun validateLogin(login: String) = when {
            login == "" -> resources.getString(R.string.login_is_empty)
            !android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches() ->
                resources.getString(R.string.not_valid_email)
            else -> null
        }

        fun validatePassword(password: String) = when {
            password == "" -> resources.getString(R.string.password_is_empty)
            password.length < 6 ->
                resources.getString(R.string.password_too_short)
            else -> null
        }

        fun setErrorTextView(textView: TextView, error: String?) {
            if (error == null) {
                textView.visibility = android.view.View.INVISIBLE
            } else {
                textView.text = error
                textView.visibility = android.view.View.VISIBLE
            }
        }

        fun logIn() {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()
            val loginError = validateLogin(login)
            val passwordError = validatePassword(password)
            setErrorTextView(loginErrorTextView, loginError)
            setErrorTextView(passwordErrorTextView, passwordError)

            // login and password are correct if
            // (loginError == null && passwordError == null)
        }

        passwordField.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER &&
                event.action == KeyEvent.ACTION_DOWN) {
                logIn()
                true
            } else {
                false
            }
        }

        verifyButton.setOnClickListener { _ -> logIn() }
    }
}