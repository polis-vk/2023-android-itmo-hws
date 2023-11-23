package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import ru.ok.itmo.example.login.LoginRequest

class NoName(val loginRequest: LoginRequest): Fragment(R.layout.message_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<ImageButton>(R.id.backline)
        back.setOnClickListener {
            loginRequest.logout()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}