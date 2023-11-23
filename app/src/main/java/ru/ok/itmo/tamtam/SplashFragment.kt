package ru.ok.itmo.tamtam

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val sharedViewModel: SharedViewModel by viewModels(ownerProducer = { requireActivity() })
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            sharedViewModel.startApp()
        }
    }
}