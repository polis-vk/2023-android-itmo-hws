package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment

class AutorizationFragment : Fragment(R.layout.authorization_fragment) {

    val account = DataAccount(1234, "geor")
    val fragment = MainFragment(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text_password = view.findViewById<EditText>(R.id.text_password)
        val text_login = view.findViewById<EditText>(R.id.text_login)
        val button = view.findViewById<Button>(R.id.bt_enter)
        text_password.addTextChangedListener {
            button.isEnabled = !(text_password.text.toString() == "" || text_login.text.toString() == "")
        }
        text_login.addTextChangedListener {
            button.isEnabled = !(text_password.text.toString() == "" || text_login.text.toString() == "")
        }
        button.setOnClickListener {
            if (account.password == text_password.text.toString().toInt() &&
                account.login == text_login.text.toString()) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .hide(this)
                    .add(R.id.fragment_container, fragment)
                    .commit()
            } else {
                Toast.makeText(requireActivity(), "Incorrect password or login", Toast.LENGTH_SHORT).show()
            }
            text_password.setText("")
            text_login.setText("")
        }

    }
}
