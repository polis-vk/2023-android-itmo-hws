package ru.ok.itmo.example.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.example.DaApplication
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentLoginBinding
import ru.ok.itmo.example.factories.LoginFactory
import ru.ok.itmo.example.models.LoginResponse


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginFactory((requireActivity().application as DaApplication).repository)
    };


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var loginResponse : LoginResponse

        viewModel.isLoggedIn()
        viewModel.isLoggedIn.observe(viewLifecycleOwner){
            if (it) {
                findNavController().navigate(R.id.action_loginFragment_to_listFragment)
            }
        }

        //Prevention of everlasting copy/paste cycle
        binding.loginEdit.setText("LetItBeRickAstley")
        binding.pwdEdit.setText("tHexxxDoloR")
        viewModel.editLogin(binding.loginEdit.text.toString())




        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_startFragment)
            }
            enterButton.setOnClickListener {
                viewModel.editLogin(loginEdit.text.toString())
                viewModel.editPwd(pwdEdit.text.toString())
                viewModel.auth()

                viewModel.navigationLive.observe(viewLifecycleOwner) {
                    loginResponse = it
                    when (loginResponse.code) {
                        200 -> {
                            findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                        }

                        401 -> {
                            binding.errorMessage.text =
                                resources.getString(R.string.unauthorized)
                        }

                        403 -> {
                            binding.errorMessage.text =
                                resources.getString(R.string.server_connection_error)
                        }

                        -1 -> {
                            binding.errorMessage.text =
                                resources.getString(R.string.unknown_error)
                        }
                    }
                }
            }

            loginEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    viewModel.editLogin(loginEdit.text.toString())
                    viewModel.isEnabledLive.observe(viewLifecycleOwner) {
                        enterButton.isEnabled = it
                    }
                }
            })


            pwdEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    viewModel.editPwd(pwdEdit.text.toString())
                    viewModel.isEnabledLive.observe(viewLifecycleOwner) {
                        enterButton.isEnabled = it
                    }
                }
            })

        }

    }

}