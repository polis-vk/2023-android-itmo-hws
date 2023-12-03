package ru.ok.itmo.example.authPart

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentAuthScreenBinding


class AuthScreen : Fragment() {

    private lateinit var binding: FragmentAuthScreenBinding
    private val loginViewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_authScreen_to_unloginScreen)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.submitButton.isEnabled = submitBtnState()
            }

            override fun afterTextChanged(s: Editable) {}
        }

        binding.loginTextInputEditText.addTextChangedListener(textWatcher)
        binding.passwordTextInputEditText.addTextChangedListener(textWatcher)

        binding.submitButton.setOnClickListener {
            loginViewModel.logIn(binding.loginTextInputEditText.text.toString(), binding.passwordTextInputEditText.text.toString())
        }

        loginViewModel.loginState.observe(viewLifecycleOwner){loginState ->
            when(loginState){
                is LoginState.Failed -> Toast.makeText(requireContext(), loginState.error_messenge, Toast.LENGTH_SHORT).show()
                is LoginState.Success -> Navigation.findNavController(view).navigate(R.id.action_authScreen_to_homePageScreen)
                else -> {}
            }
        }

        binding.forgotPassword.setOnClickListener {
            Toast.makeText(requireContext(), "В разработке", Toast.LENGTH_SHORT).show()
        }

    }

    fun submitBtnState(): Boolean {
        return !(binding.loginTextInputEditText.text.isNullOrBlank() || binding.passwordTextInputEditText.text.isNullOrBlank())
    }

}