package ru.ok.itmo.tamtam.shared_model

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.koin.core.component.KoinComponent
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.start.SplashFragment
import ru.ok.itmo.tamtam.start.SplashType

class SharedViewModel : ViewModel(), KoinComponent {
    lateinit var navController: NavController

    fun closeAll() {
        navController.popBackStack(R.id.start_nav_graph, true)
        val bundle = Bundle().apply {
            putSerializable(SplashFragment.ARG_SPLASH_TYPE, SplashType.LOGOUT_TYPE)
        }
        navController.navigate(R.id.start_nav_graph, bundle)
    }
}