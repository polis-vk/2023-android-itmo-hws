package ru.ok.itmo.tamtam.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.Helper
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.SharedViewModel
import ru.ok.itmo.tamtam.server.ServerException

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels(ownerProducer = { requireActivity() })

    private lateinit var editTextLogin: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var textLayoutLogin: TextInputLayout
    private lateinit var textLayoutPassword: TextInputLayout
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var arrowLeftBack: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.let { Helper.setLightStatusBar(requireContext(), it) }

        editTextLogin = view.findViewById(R.id.edit_text_login)
        editTextPassword = view.findViewById(R.id.edit_text_password)
        textLayoutLogin = view.findViewById(R.id.text_layout_login)
        textLayoutPassword = view.findViewById(R.id.text_layout_password)

        btnLogin = view.findViewById(R.id.btn_login)
        progressBar = view.findViewById(R.id.progress_bar)
        forgotPasswordTextView = view.findViewById(R.id.forgot_password_text_view)
        arrowLeftBack = view.findViewById(R.id.arrow_left_back)

        navBarLogic()
        forgotPasswordLogic()
        keyboardLogic()
        editTextLoginLogic()
        editTextPasswordLogic()
        btnLoginLogic()
        authorizationLogic()
    }

    private fun forgotPasswordLogic() {
        forgotPasswordTextView.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionFragmentLoginToForgotPasswordFragment())
        }
    }

    private fun authorizationLogic() {
        val tokenObserver = Observer<String> { token ->
            successAuth(token)
        }
        viewModel.token.observe(viewLifecycleOwner, tokenObserver)
    }

    private fun btnChangeState(state: Boolean) {
        btnLogin.isEnabled = state
        Helper.setButtonColor(
            btnLogin, requireContext(), if (state) R.color.buttons else R.color.disabled_button
        )
    }

    private fun btnLoginLogic() {
        val enableObserver = Observer<Boolean> { state ->
            btnChangeState(state)
        }
        viewModel.readyForAuthorization.observe(viewLifecycleOwner, enableObserver)

        btnLogin.setOnClickListener {
            showLoading()
            lifecycleScope.launch {
                try {
                    viewModel.authorization()
                } catch (e: ServerException) {
                    when (e) {
                        ServerException.Unauthorized -> errorUnauthorized()
                    }
                } catch (e: Exception) {
                    showToastInFragment(e.message ?: "Unknown error")
                    hideLoading()
                }
            }
        }
    }

    private fun showLoading() {
        btnLogin.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        btnLogin.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    private fun errorUnauthorized() {
        showToastInFragment(getString(R.string.error_unauthorized))
        textLayoutLogin.error = getString(R.string.error_unauthorized)
        textLayoutPassword.error = getString(R.string.error_unauthorized)
        hideLoading()
    }

    private fun successAuth(token: String) {
        sharedViewModel.login(token)
        showToastInFragment(getString(R.string.success_auth))
        findNavController().navigate(LoginFragmentDirections.actionFragmentLoginToAppNavGraph())
        hideLoading()
    }

    private fun showToastInFragment(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun keyboardLogic() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun navBarLogic() {
        arrowLeftBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun editTextLoginLogic() {
        editTextLogin.addTextChangedListener { text ->
            viewModel.changeLogin(text.toString())
        }
    }

    private fun editTextPasswordLogic() {
        editTextPassword.addTextChangedListener { text ->
            viewModel.changePassword(text.toString())
        }

        editTextPassword.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                btnLogin.performClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
}