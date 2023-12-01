package ru.ok.itmo.tuttut.login.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.LOGIN
import ru.ok.itmo.tuttut.MainActivity
import ru.ok.itmo.tuttut.PASSWORD
import ru.ok.itmo.tuttut.R
import ru.ok.itmo.tuttut.dataStore
import ru.ok.itmo.tuttut.databinding.FragmentLoginBinding
import ru.ok.itmo.tuttut.login.domain.LoginState

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    private fun actionBar() = (activity as MainActivity?)?.supportActionBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.logout()
        actionBar()?.apply {
            setTitle(R.string.log_in)
            setDisplayHomeAsUpEnabled(false)
        }
        binding.loginButton.setOnClickListener {
            loginViewModel.login(binding.login.text.toString(), binding.password.text.toString())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            context?.dataStore?.data?.first().let {
                binding.login.setText(it?.get(LOGIN))
                binding.password.setText(it?.get(PASSWORD))
            }
            loginViewModel.loginState.collect {
                Log.d("TAG", it.toString())
                if (it is LoginState.Loading) {
                    actionBar()?.setTitle(R.string.loading)
                }
                if (it is LoginState.Failure) {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
                if (it is LoginState.Success) {
                    val navController = findNavController()
                    navController.navigate(
                        LoginFragmentDirections.actionLoginFragmentToChatsFragment()
                    )
                    actionBar()?.setDisplayHomeAsUpEnabled(true)
                }
            }
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
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}