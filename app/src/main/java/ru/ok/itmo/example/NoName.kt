package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class NoName: Fragment(R.layout.no_name) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<Button>(R.id.back_button)
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}