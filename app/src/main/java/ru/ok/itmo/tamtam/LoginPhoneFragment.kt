package ru.ok.itmo.tamtam

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class LoginPhoneFragment : Fragment(R.layout.fragment_login_phone) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.let { Helper.setLightStatusBar(requireContext(), it) }
    }
}