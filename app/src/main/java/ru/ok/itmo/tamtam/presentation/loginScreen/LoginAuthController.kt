package ru.ok.itmo.tamtam.presentation.loginScreen

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.asTransaction
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.common.Constants.SHARED
import ru.ok.itmo.tamtam.common.Constants.TOKEN
import ru.ok.itmo.tamtam.presentation.viewmodels.LoginAuthViewModel

class LoginAuthController : Controller() {

    private val viewModel = LoginAuthViewModel()
    private var loginTextField: TextInputEditText? = null
    private var passwordTextField: TextInputEditText? = null
    private var continueButton: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.login_auth_screen, container, false)
        val activity = router.activity as AppCompatActivity

        loginTextField = view.findViewById(R.id.login_field)
        passwordTextField = view.findViewById(R.id.password_field)
        continueButton = view.findViewById(R.id.continue_button)

        activity.lifecycleScope.launch {
            signOnViewModel(activity)
        }

        continueButton?.setOnClickListener {
            val login = loginTextField?.text.toString()
            val pwd = passwordTextField?.text.toString()
            viewModel.tryLogin(login, pwd)

        }

        passwordTextField?.doOnTextChanged { text, _, _, _ ->
            viewModel.updatePassword(text.toString())
        }

        loginTextField?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateLogin(text.toString())
        }

        // back button
        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            router.handleBack()
        }

        // forget password button
        view.findViewById<TextView>(R.id.forget_button).setOnClickListener {
            Toast.makeText(
                container.context,
                R.string.you_lose,
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }

    private suspend fun signOnViewModel(activity: AppCompatActivity) {
        viewModel.state.collect {
            when (it) {
                is LoginScreenState.Error -> {
                    Toast.makeText(activity.baseContext, it.message, Toast.LENGTH_SHORT).show()
                }

                is LoginScreenState.Success -> {
                    loginTextField?.text = SpannableStringBuilder(it.login)
                    loginTextField?.setSelection(it.login.length)
                    passwordTextField?.text = SpannableStringBuilder(it.pwd)
                    passwordTextField?.setSelection(it.pwd.length)
                    continueButton?.isEnabled = it.continueButtonState
                }

                is LoginScreenState.Navigate -> {
                    activity.baseContext.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
                        .edit().putString(
                            TOKEN, it.token
                        ).apply()
                    router.pushController(it.route.asTransaction())
                }
            }
        }
    }

    override fun onDestroyView(view: View) {
        loginTextField = null
        passwordTextField = null
        continueButton = null
        super.onDestroyView(view)
    }

}