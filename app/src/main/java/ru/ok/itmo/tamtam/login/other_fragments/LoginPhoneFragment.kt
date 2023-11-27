package ru.ok.itmo.tamtam.login.other_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.ok.itmo.tamtam.helper.Helper
import ru.ok.itmo.tamtam.R

class LoginPhoneFragment : Fragment(R.layout.fragment_login_phone) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.let { Helper.setLightStatusBar(requireContext(), it) }
    }
}