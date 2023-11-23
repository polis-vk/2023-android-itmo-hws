package ru.ok.itmo.tamtam.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.databinding.FragmentLaunchBinding
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import ru.ok.itmo.tamtam.utils.getThemeColor
import ru.ok.itmo.tamtam.utils.setStatusBarTextDark

class LaunchFragment : FragmentWithBinding<FragmentLaunchBinding>(FragmentLaunchBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        initListeners()
    }

    private fun setupStatusBar() {
        requireActivity().window.statusBarColor =
            requireContext().getThemeColor(androidx.appcompat.R.attr.colorAccent)
        requireActivity().setStatusBarTextDark(false)
    }

    private fun initListeners() {
        binding.loginWithBasicBtn.setOnClickListener {
            findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToLoginFragment())
        }
    }
}