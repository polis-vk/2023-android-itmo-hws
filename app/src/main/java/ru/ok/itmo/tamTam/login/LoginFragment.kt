package ru.ok.itmo.tamTam.login

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.ok.itmo.tamTam.AuthInfo
import ru.ok.itmo.tamTam.CustomException
import ru.ok.itmo.tamTam.R
import java.net.SocketTimeoutException

class LoginFragment : Fragment(R.layout.login_fragment) {

    private var login: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var passwordLayout: TextInputLayout? = null
    private var signIn: Button? = null
    private var toolbar: MaterialToolbar? = null
    private var loading: ProgressBar? = null
    private var errorInfo: TextView? = null

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onStart() {
        loginViewModel.logout()
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = findNavController()

        login = view.findViewById(R.id.login)
        password = view.findViewById(R.id.password_input)
        passwordLayout = view.findViewById(R.id.password_layout)
        signIn = view.findViewById(R.id.sign_in)
        toolbar = view.findViewById(R.id.toolbar)
        loading = view.findViewById(R.id.loading)
        errorInfo = view.findViewById(R.id.error_info)

        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            val exception = result.exceptionOrNull()
            if (result.isSuccess) {
                AuthInfo.token = result.getOrNull()!!
                controller.navigate(R.id.chatsFragment)
                resetUi()
            } else if (exception != null && exception !is CustomException) {
                updateLoadingVisibility(false)
                errorInfo!!.text = getString(
                    when (exception) {
                        is SocketTimeoutException -> R.string.no_internet
                        is retrofit2.HttpException -> R.string.invalid_input
                        else -> R.string.external_error
                    }
                )
                errorInfo!!.visibility = View.VISIBLE
            }
        }

        toolbar!!.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        signIn!!.setOnClickListener {
            updateLoadingVisibility(true)
            loginViewModel.login(User(login!!.text.toString(), password!!.text.toString()))
        }

        passwordLayout!!.setEndIconOnClickListener {
            updatePasswordIcon()
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isButtonEnabled = !login!!.text.isNullOrBlank() && !password!!.text.isNullOrBlank()
                signIn!!.isEnabled = isButtonEnabled
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        }

        login!!.addTextChangedListener(textWatcher)
        password!!.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        login = null
        password = null
        passwordLayout = null
        signIn = null
        toolbar = null
        loading = null
        errorInfo = null
    }

    private fun updateLoadingVisibility(isVisible: Boolean) {
        loading!!.visibility = if (isVisible) View.VISIBLE else View.GONE
        signIn!!.isEnabled = !isVisible
    }

    private fun updatePasswordIcon() {
        val isPasswordVisible = password!!.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        password!!.inputType =
            if (isPasswordVisible) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        val iconResId = if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_inv_eye
        passwordLayout!!.setEndIconDrawable(iconResId)
    }

    private fun resetUi() {
        login!!.text = null
        password!!.text = null
        password!!.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
}
