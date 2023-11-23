package ru.ok.itmo.example.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import ru.ok.itmo.example.MainViewModel
import ru.ok.itmo.example.R

class StartedFragment : Fragment(R.layout.fragment_started) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_startedFragment_to_loginFragment)
        }
    }
}