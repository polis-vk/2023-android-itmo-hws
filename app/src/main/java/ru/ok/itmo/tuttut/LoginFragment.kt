package ru.ok.itmo.tuttut

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.databinding.FragmentLoginBinding
import ru.ok.itmo.tuttut.login.domain.LoginState
import ru.ok.itmo.tuttut.login.ui.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.logout()
        binding.loginButton.setOnClickListener {
            loginViewModel.login(binding.login.text.toString(), binding.password.text.toString())
        }
        lifecycleScope.launch {
            loginViewModel.loginState.onEach {
                if (it is LoginState.Success || it is LoginState.Failure) {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
                if (it is LoginState.Success) {
                    parentFragmentManager.commit {
                        replace(
                            R.id.fragment_container,
                            BlankFragment.newInstance("abobik", "abobus"),
                            BlankFragment.TAG
                        )
                        addToBackStack(BlankFragment.TAG)
                    }
                }
            }.stateIn(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG: String = "login_fragment"

        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}