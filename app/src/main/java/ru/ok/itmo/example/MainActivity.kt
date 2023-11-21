package ru.ok.itmo.example

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                    NavHostFragment).navController

        if (savedInstanceState == null) {
            sharedViewModel.startApp()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBundle("nav_state", sharedViewModel.navController.saveState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        sharedViewModel.navController.restoreState(savedInstanceState.getBundle("nav_state"))
    }
}