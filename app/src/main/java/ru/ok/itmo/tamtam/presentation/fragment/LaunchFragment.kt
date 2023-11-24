package ru.ok.itmo.tamtam.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.databinding.FragmentLaunchBinding
import ru.ok.itmo.tamtam.utils.FragmentWithBinding

class LaunchFragment : FragmentWithBinding<FragmentLaunchBinding>(FragmentLaunchBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners(){
        binding.loginWithBasicBtn.setOnClickListener {
            findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToLoginFragment())
        }
    }
}