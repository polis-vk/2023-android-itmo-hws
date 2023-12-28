package ru.ok.itmo.example.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.ok.itmo.example.R

class EnterFragment : Fragment(R.layout.entrer_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun toast(message: String) = Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()

        view.findViewById<Button>(R.id.enterByPhone)?.setOnClickListener {
            toast("Not supported now")
        }

        view.findViewById<TextView>(R.id.enterByLogin).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.screen_container, LoginFragment(), "login")
                .addToBackStack("login")
                .commit()
        }
    }
}