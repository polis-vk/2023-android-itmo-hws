package ru.ok.itmo.tamtam

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class SharedViewModel : ViewModel() {
    companion object {
        const val DEFAULT_TOKEN = "default_token"
    }

    lateinit var navController: NavController
    private var token: String = DEFAULT_TOKEN

    fun logout() {
        token = DEFAULT_TOKEN
        closeAll()
    }

    fun login(newToken: String) {
        token = newToken
    }

    fun startApp() {
        if (token == DEFAULT_TOKEN) {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginNavGraph())
        } else {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToAppNavGraph())
        }
    }

    private fun closeAll() {
        navController.popBackStack(R.id.start_nav_graph, true)
        navController.navigate(R.id.start_nav_graph)
        startApp()
    }
}