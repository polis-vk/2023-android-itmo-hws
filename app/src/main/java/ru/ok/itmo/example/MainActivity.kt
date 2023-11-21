package ru.ok.itmo.example

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        private const val MIN_EMAIL_FIELD_SIZE = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val emailField = findViewById<EditText>(R.id.email_input)
        val passwordField = findViewById<EditText>(R.id.password_input)
        val enterButton = findViewById<Button>(R.id.button_enter)
        val showPasswordButton = findViewById<Button>(R.id.button_show_password)
        val changeThemeButton = findViewById<Button>(R.id.button_change_theme)
        var isPasswordShowed = false

        enterButton.setOnClickListener {
            if (emailField.text.isBlank()) {
                makeWidget(R.string.error_empty_emil)
            } else if (emailField.text.count() < MIN_EMAIL_FIELD_SIZE) {
                makeWidget(R.string.error_few_chars)
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString())
                    .matches()
            ) {
                makeWidget(R.string.error_wrong_email)
            } else if (passwordField.text.toString() == "") {
                makeWidget(R.string.error_empty_password)
            } else {
                makeWidget(R.string.login)
            }
        }

        passwordField.setOnKeyListener { v, keyCode, event ->
            when {
                (keyCode == KeyEvent.KEYCODE_ENTER) -> {
                    enterButton.performClick()
                    closeKeyboard()
                    return@setOnKeyListener true
                }

                else -> false
            }
        }

        showPasswordButton.setOnClickListener {
            if (isPasswordShowed) {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                showPasswordButton.text = getString(R.string.button_show_password)
            } else {
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPasswordButton.text = getString(R.string.button_hide_password)
            }
            isPasswordShowed = !isPasswordShowed
        }


        changeThemeButton.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

        }

    }


    private fun makeWidget(intText: Int) {
        val toast = Toast.makeText(this, getString(intText), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }

}