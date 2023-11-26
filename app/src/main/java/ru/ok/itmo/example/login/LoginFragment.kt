package ru.ok.itmo.example.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.ok.itmo.example.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.example.login.repository.LoginState

class LoginFragment : Fragment(R.layout.login_fragment) {
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (loginViewModel.isLogin()) {
            loginViewModel.logout()
        }

        val loginField = view.findViewById<TextInputEditText>(R.id.loginInputEdit)
        val passwordField = view.findViewById<TextInputEditText>(R.id.passwordInputEdit)

        view.findViewById<ImageView>(R.id.login_back_button).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            Log.d(TAG, "login")
            loginViewModel.login((loginField.text ?: "").toString(), (passwordField.text ?: "").toString())
        }

        lifecycleScope.launch {
            loginViewModel.status.onEach {
                Log.d(TAG, "New status: $it")
                if (it is LoginState.Success) {
                    Log.d(TAG, "NAVIGATE")
                    findNavController().navigate(R.id.action_loginFragment_to_chatsFragment)
                } else if (it is LoginState.Failure) {
                    Toast.makeText(context, R.string.login_failure_toast, Toast.LENGTH_SHORT).show()
                }
            }.stateIn(this)
        }
    }

    companion object {
        const val TAG = "LOGIN_FRAGMENT"
    }
}