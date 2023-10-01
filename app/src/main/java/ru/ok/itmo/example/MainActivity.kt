package ru.ok.itmo.example

import android.content.res.Configuration
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var button1NightMode: Button
    private lateinit var button2NightMode: Button
    private lateinit var button1Translator: Button
    private lateinit var button2Translator: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private var isEnglish = true
    private var userEmail: String = ""
    private var userPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val togglePasswordVisibility: ToggleButton = findViewById(R.id.togglePasswordVisibility)

        togglePasswordVisibility.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString("userEmail", "")
            userPassword = savedInstanceState.getString("userPassword", "")
            isEnglish = savedInstanceState.getBoolean("isEnglish")
        }

        val nightModeButton = findViewById<Button>(R.id.application_theme)
        button1NightMode = findViewById(R.id.Black)
        button2NightMode = findViewById(R.id.White)

        nightModeButton.setOnClickListener {
            if (button1NightMode.visibility == View.GONE) {
                button1NightMode.visibility = View.VISIBLE
                button2NightMode.visibility = View.VISIBLE
            } else {
                button1NightMode.visibility = View.GONE
                button2NightMode.visibility = View.GONE
            }
        }

        val translatorButton = findViewById<Button>(R.id.Translator)
        button1Translator = findViewById(R.id.English)
        button2Translator = findViewById(R.id.Arabian)

        translatorButton.setOnClickListener {
            if (button1Translator.visibility == View.GONE) {
                button1Translator.visibility = View.VISIBLE
                button2Translator.visibility = View.VISIBLE
            } else {
                button1Translator.visibility = View.GONE
                button2Translator.visibility = View.GONE
            }
        }

        setupArabianButton()
        setupEnglishButton()
        updateButtonLabels()

        emailEditText = findViewById(R.id.Email)
        passwordEditText = findViewById(R.id.Password)
        loginButton = findViewById(R.id.Enter)

        loginButton.setOnClickListener {
            performLogin()
        }

        findViewById<Button>(R.id.Black).setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        findViewById<Button>(R.id.White).setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun performLogin() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val emailPattern = "[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}".toRegex()

        when {
            email.isEmpty() -> showMessage("empty email")
            !email.matches(emailPattern) -> showMessage("incorrect email")
            password.isEmpty() -> showMessage("empty password")
            password.length < 6 -> showMessage("The password must be at least 6 characters long")
            else -> {
                showMessage("you are logged in")
            }
        }
    }


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun updateButtonLabels() {
        val translatorButton = findViewById<Button>(R.id.Translator)
        val englishButton = findViewById<Button>(R.id.English)
        val arabianButton = findViewById<Button>(R.id.Arabian)
        val modeButton = findViewById<Button>(R.id.application_theme)
        val blackButton = findViewById<Button>(R.id.Black)
        val whiteButton = findViewById<Button>(R.id.White)
        val enterButton = findViewById<Button>(R.id.Enter)
        val emailButton = findViewById<EditText>(R.id.Email)
        val passwordButton = findViewById<EditText>(R.id.Password)

        translatorButton.text = getString(R.string.modeTranslator)
        englishButton.text = getString(R.string.englishButton)
        arabianButton.text = getString(R.string.arabianButton)
        modeButton.text = getString(R.string.applicationthemeButton)
        blackButton.text = getString(R.string.blackButton)
        whiteButton.text = getString(R.string.whiteButton)
        enterButton.text = getString(R.string.loginString)
        emailButton.hint = getString(R.string.inputEmail)
        passwordButton.hint = getString(R.string.inputPassword)
    }


    private fun setupArabianButton() {
        isEnglish = false;
        val arabianButton = findViewById<Button>(R.id.Arabian)
        arabianButton.setOnClickListener {
            changeToArabian()
        }
    }

    private fun setupEnglishButton() {
        isEnglish = true;
        val englishButton = findViewById<Button>(R.id.English)
        englishButton.setOnClickListener {
            changeToEnglish()
        }
    }

    private fun changeToEnglish() {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        updateButtonLabels()
    }

    private fun changeToArabian() {
        val locale = Locale("ar")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        updateButtonLabels()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("userEmail", userEmail)
        outState.putString("userPassword", userPassword)
        outState.putBoolean("isEnglish", isEnglish)
        super.onSaveInstanceState(outState)
    }
}
