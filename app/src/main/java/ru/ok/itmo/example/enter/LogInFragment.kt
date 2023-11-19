package ru.ok.itmo.example.enter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chat.ChatViewModel
import ru.ok.itmo.example.databinding.FragmentEnterBinding
import ru.ok.itmo.example.domain.LoginState
import ru.ok.itmo.example.domain.UseCase
import ru.ok.itmo.example.retrofit.RetrofitProvider
import ru.ok.itmo.example.retrofit.regApi

class LogInFragment : Fragment(R.layout.fragment_enter) {
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentEnterBinding

    private val loginUseCase by lazy {
        val retrofit = RetrofitProvider.retrofit
        val weatherApi = regApi.provideRequestApi(retrofit)
        UseCase(weatherApi)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginEditText.setText(viewModel.name)
        binding.passwordEditText.setText(viewModel.password)

        binding.loginEditText.addTextChangedListener {
            viewModel.name = binding.loginEditText.text.toString()
            viewModel.validateCredentials(viewModel.name, viewModel.password)
        }
        binding.passwordEditText.addTextChangedListener {
            viewModel.password = binding.passwordEditText.text.toString()
            viewModel.validateCredentials(viewModel.name, viewModel.password)
        }


        viewModel.isCorrect.observe(viewLifecycleOwner) { isCorrect ->
            if (isCorrect) {
                binding.enterButton.visibility = View.VISIBLE
            } else {
                binding.enterButton.visibility = View.GONE
            }
        }

        binding.enterButton.setOnClickListener {
            viewModel.name = binding.loginEditText.text.toString()
            viewModel.password = binding.passwordEditText.text.toString()
            val flag = isInternetAvailable()
            if (flag) {
                MainScope().launch(Dispatchers.IO) {
                    val loginState = loginUseCase.getLogInfo(viewModel.name, viewModel.password)
                    withContext(Dispatchers.Main) {
                        handleLoginState(viewModel, loginState)
                    }
                }
            } else {
                binding.errorMessage.text = LoginState.typeError.Unknown_Error.toString()
            }
        }
    }

    private fun handleLoginState(viewModel: LoginViewModel, loginState: LoginState) {
        try {
            when (loginState) {
                is LoginState.Success -> {
                    viewModel.token = MutableLiveData(loginState.token)
                    val chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
                    chatViewModel.setToken(loginState.token)
                    findNavController().navigate(R.id.action_fragment_enter_to_fragment_chat)
                }

                is LoginState.Error -> {
                    val errorMessage = LoginState.errorMap[loginState.error] ?: "Unknown Error"
                    binding.errorMessage.text = errorMessage
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
