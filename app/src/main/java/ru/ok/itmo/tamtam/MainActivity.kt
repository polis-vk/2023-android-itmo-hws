package ru.ok.itmo.tamtam

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.data.AuthRepository
import ru.ok.itmo.tamtam.utils.OnBackPressed
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main), OnBackPressed {
    @Inject
    lateinit var authRepository: AuthRepository
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        if (savedInstanceState == null) {
            checkAuth()
        }
        observeAuth()
    }

    private fun checkAuth() {
        lifecycleScope.launch {
            if (authRepository.isAuth.first()) {
                navController
                    ?.navigate(AppNavigationDirections.actionToChatsFragment())
            }
        }
    }

    private suspend fun hasFragmentInNavBackStack(name: String): Boolean =
        navController?.currentBackStack?.first()?.map { it.destination.label }?.contains(name)
            ?: false

    private fun observeAuth() {
        lifecycleScope.launch {
            authRepository.isAuth.collect {
                if (!it && !hasFragmentInNavBackStack("LaunchFragment")) {
                    navController
                        ?.navigate(AppNavigationDirections.actionToLaunchFragment())
                }
            }
        }
    }

    override fun addCustomOnBackPressed(lifecycleOwner: LifecycleOwner, body: () -> Unit) {
        onBackPressedDispatcher.addCallback(lifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                body.invoke()
            }
        })
    }
}