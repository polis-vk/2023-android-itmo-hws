package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.data.repository.AuthRepository
import ru.ok.itmo.tamtam.databinding.FragmentLoginBinding
import ru.ok.itmo.tamtam.presentation.stateholder.LoginViewModel
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import ru.ok.itmo.tamtam.utils.getThemeColor
import ru.ok.itmo.tamtam.utils.observeNotifications
import ru.ok.itmo.tamtam.utils.setStatusBarTextDark
import javax.inject.Inject

class LoginFragment : FragmentWithBinding<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (this.requireActivity().application as App).appComponent.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        initListeners()
        observerCredentials()
        this.requireContext()
            .observeNotifications(viewLifecycleOwner.lifecycleScope, loginViewModel.notifications)
    }

    private fun setupStatusBar() {
        requireActivity().window.statusBarColor =
            requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary)
        requireActivity().setStatusBarTextDark(true)
    }

    private fun observerCredentials() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            loginViewModel.credentials.collect {
                withContext(Dispatchers.Main) {
                    binding.loginBtn.isEnabled = it.isValid
                    binding.passwordTextInputLayout.isErrorEnabled = it.isBad
                    binding.loginTextInputLayout.isErrorEnabled = it.isBad
                    binding.passwordTextInputLayout.error =
                        if (it.isBad) getString(R.string.invalid_login_or_password)
                        else getString(R.string.empty_string)
                    binding.loginTextInputLayout.error =
                        if (it.isBad) getString(R.string.invalid_login_or_password)
                        else getString(R.string.empty_string)
                }
            }
        }
    }

    private fun initListeners() {
        binding.loginBtn.setOnClickListener {
            loginViewModel.login {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatsFragment())
            }
        }
        binding.loginTe.addTextChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                loginViewModel.setLogin(it.toString())
            }
        }
        binding.passwordTe.addTextChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                loginViewModel.setPassword(it.toString())
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}