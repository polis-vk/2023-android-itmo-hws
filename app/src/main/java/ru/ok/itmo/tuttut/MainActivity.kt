package ru.ok.itmo.tuttut

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, LoginFragment.newInstance(), LoginFragment.TAG)
            addToBackStack(LoginFragment.TAG)
        }
    }
}