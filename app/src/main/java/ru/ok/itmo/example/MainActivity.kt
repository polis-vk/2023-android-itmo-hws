package ru.ok.itmo.example

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var pageTitleView: TextView
    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var authorizationButtonView: Button
    private lateinit var themeSwitcher: Switch
    private lateinit var visionBoxPassword: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pageTitleView = findViewById(R.id.pageTitle)
        emailTextView = findViewById(R.id.textEmailAddress)
        passwordTextView = findViewById(R.id.textPassword)
        authorizationButtonView = findViewById(R.id.authorizationButton)
        themeSwitcher = findViewById(R.id.themeSwitcher)
        visionBoxPassword = findViewById(R.id.passwordVisibility)

        visionBoxPassword.setOnCheckedChangeListener { _, check ->
            if (check) {
                passwordTextView.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                passwordTextView.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            passwordTextView.setSelection(passwordTextView.text.length)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            themeSwitcher.visibility = View.VISIBLE
        } else {
            themeSwitcher.visibility = View.GONE
        }

        authorizationButtonView.setOnClickListener {
            val emailRes = parseEmail(emailTextView.text)
            val passwordRes = parsePassword(passwordTextView.text)

            if(emailRes == getString(R.string.correct_input) && passwordRes != getString(R.string.correct_input)){
                Toast.makeText(this, passwordRes, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, emailRes, Toast.LENGTH_LONG).show()
            }
        }

        themeSwitcher.setOnCheckedChangeListener { _, check ->
            if (check) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }

    private fun parseEmail(email: Editable): String{
        if(email.isEmpty()){
            return getString(R.string.error_empty)
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return getString(R.string.error_input_email)
        }
        return getString(R.string.correct_input)
    }

    private fun parsePassword(password: Editable): String{
        if(password.isEmpty()){
            return getString(R.string.error_empty)
        }else if (password.length < 4){
            return getString(R.string.error_input_password)
        }
        return getString(R.string.correct_input)
    }
}