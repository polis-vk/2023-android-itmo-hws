package ru.ok.itmo.example.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import ru.ok.itmo.example.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.login_fragment) {
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (loginViewModel.isLogin()) {
            loginViewModel.logout()
        }

        val loginField = view.findViewById<TextInputEditText>(R.id.loginInputEdit)
        val passwordField = view.findViewById<TextInputEditText>(R.id.passwordInputEdit)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        loginButton.isEnabled = false

        view.findViewById<ImageView>(R.id.login_back_button).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        loginField.addTextChangedListener {
            loginButton.isEnabled =
                loginField.text?.isNotEmpty() ?: false && passwordField.text?.isNotEmpty() ?: false
        }
        passwordField.addTextChangedListener {
            loginButton.isEnabled =
                loginField.text?.isNotEmpty() ?: false && passwordField.text?.isNotEmpty() ?: false
        }

        loginButton.setOnClickListener {
            Log.d(TAG, "login")
            loginViewModel.login(
                (loginField.text ?: "").toString(),
                (passwordField.text ?: "").toString()
            )
        }

        loginViewModel.state.onEach {
            Log.d(TAG, "New status: $it")
            if (it is LoginState.Failure) {
                Toast.makeText(context, R.string.login_failure_toast, Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        loginViewModel.effect.onEach {
            if (it is LoginEvents.Navigate) {
                Log.d(TAG, "NAVIGATE")
                findNavController().navigate(R.id.action_loginFragment_to_chatsFragment)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        const val TAG = "LOGIN_FRAGMENT"
    }
}