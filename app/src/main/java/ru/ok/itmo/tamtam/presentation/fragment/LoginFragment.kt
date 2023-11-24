package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.AppNavigationDirections
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.data.AuthRepository
import ru.ok.itmo.tamtam.databinding.FragmentLoginBinding
import ru.ok.itmo.tamtam.utils.NotificationType
import ru.ok.itmo.tamtam.presentation.stateholder.LoginViewModel
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import javax.inject.Inject

class LoginFragment : FragmentWithBinding<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        loginViewModel =
            ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observerCredentials()
        observeNotifications()
    }

    private fun observeNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            for (notification in loginViewModel.notifications) {
                notification.printStackTrace()
                when (notification) {
                    is NotificationType.Client ->
                        Toast.makeText(
                            this@LoginFragment.context,
                            getString(R.string.client_exception_message),
                            Toast.LENGTH_SHORT
                        ).show()

                    is NotificationType.Connection -> Toast.makeText(
                        this@LoginFragment.context,
                        getString(R.string.connection_exception_message),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NotificationType.Server -> Toast.makeText(
                        this@LoginFragment.context,
                        getString(R.string.server_exception_message),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NotificationType.Unauthorized -> Toast.makeText(
                        this@LoginFragment.context,
                        getString(R.string.unauthorized_exception_message),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NotificationType.Unknown -> Toast.makeText(
                        this@LoginFragment.context,
                        getString(R.string.unknown_exception_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observerCredentials() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.credentials.collect {
                binding.loginBtn.isEnabled = it.isValid
            }
        }
    }

    private val mutexLoginBtn: Mutex = Mutex(false)

    private fun initListeners() {
        binding.loginBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (mutexLoginBtn.isLocked) return@launch
                mutexLoginBtn.withLock {
                    loginViewModel.login {
                        findNavController().navigate(AppNavigationDirections.actionToChatsFragment())
                    }
                }
            }
        }
        binding.loginTe.addTextChangedListener {
            CoroutineScope(Dispatchers.IO).launch {
                loginViewModel.setLogin(it.toString())
            }
        }
        binding.passwordTe.addTextChangedListener {
            CoroutineScope(Dispatchers.IO).launch {
                loginViewModel.setPassword(it.toString())
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}