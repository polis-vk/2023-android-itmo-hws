package ru.ok.itmo.example.view


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import ru.ok.itmo.example.MainViewModel
import ru.ok.itmo.example.R

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: MainViewModel by activityViewModels()
    lateinit var button: Button
    lateinit var loginField: TextInputEditText
    lateinit var passwordField: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.simpleActionBar!!.title = getString(R.string.action_bar_login)

        button = view.findViewById<Button?>(R.id.buttonSignIn).apply {
            setOnClickListener {
                viewModel.verifyAndLogin(
                    loginField.text.toString(),
                    passwordField.text.toString()
                )
            }
            isEnabled = false
        }

        loginField = view.findViewById<TextInputEditText?>(R.id.editTextLogin).apply {
            doOnTextChanged { text, start, before, count ->
                enableButtonIfNeed()
            }
        }
        passwordField = view.findViewById<EditText?>(R.id.editTextPassword).apply {
            doOnTextChanged { text, start, before, count ->
                enableButtonIfNeed()
            }
        }
    }

    fun enableButtonIfNeed() {
        button.isEnabled = (loginField.text!!.isNotEmpty() && passwordField.text!!.isNotEmpty())
    }
}