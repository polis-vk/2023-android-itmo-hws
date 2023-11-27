package ru.ok.itmo.tamtam.login.other_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.helper.Helper
import ru.ok.itmo.tamtam.R

class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.let { Helper.setAccentStatusBar(requireContext(), it) }

        view.findViewById<Button>(R.id.btn_login_authorization).setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginFragment())
        }

        view.findViewById<Button>(R.id.btn_phone_authorization).setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginPhoneFragment())
        }
    }
}