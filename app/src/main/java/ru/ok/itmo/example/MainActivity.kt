package ru.ok.itmo.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val switchTheme: Switch = findViewById(R.id.switchTheme)

        signIn()

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun signIn() {
        val textInputLogin = findViewById<TextInputEditText>(R.id.textInputLogin)
        val textInputPassword = findViewById<TextInputEditText>(R.id.textInputPassword)
        val textInputLoginLayout = findViewById<TextInputLayout>(R.id.textInputLoginLayout)
        val textInputPasswordLayout = findViewById<TextInputLayout>(R.id.textInputPasswordLayout)
        val btnEnter = findViewById<Button>(R.id.signInButton)

        btnEnter.setOnClickListener {
            val login = textInputLogin.text.toString()
            val password = textInputPassword.text.toString()

            if (login.isEmpty()) {
                textInputLoginLayout.error = getString(R.string.error_empty_login)
                textInputPasswordLayout.error = null
            } else if (!loginValidation(login)) {
                textInputLoginLayout.error = getString(R.string.error_incorrect_login)
                textInputPasswordLayout.error = null
            } else if (password.isEmpty()) {
                textInputLoginLayout.error = null
                textInputPasswordLayout.error = getString(R.string.error_empty_password)
            } else if (password.length < 6) {
                textInputLoginLayout.error = null
                textInputPasswordLayout.error = getString(R.string.error_password_lenght)
            } else{
                textInputPasswordLayout.error = null
                Toast.makeText(this, getString(R.string.successful), Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun loginValidation(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}