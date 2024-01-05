package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ok.itmo.example.login.LoginViewModel

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: LoginViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        view.findViewById<AppCompatImageButton>(R.id.signOut).setOnClickListener {
            viewModel.logout()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartedFragment())
                .commit()
        }
    }
}