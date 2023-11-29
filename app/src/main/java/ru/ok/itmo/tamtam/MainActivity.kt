package ru.ok.itmo.tamtam

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.data.repository.AuthRepository
import ru.ok.itmo.tamtam.utils.OnBackPressed
import ru.ok.itmo.tamtam.utils.orFalse
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnBackPressed {
    @Inject
    lateinit var authRepository: AuthRepository

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            checkAuth()
        }
        observeAuth()
    }

    private fun checkAuth() {
        if (authRepository.isAuth) {
            navController
                .navigate(AppNavigationDirections.actionFromUnauthorizedGraphToChatsFragment())
        }
    }

    private suspend fun hasFragmentInNavBackStack(name: String): Boolean =
        navController.currentBackStack
            .first()
            .map { it.destination.label }
            .contains(name)
            .orFalse()

    private fun observeAuth() {
        lifecycleScope.launch(Dispatchers.IO) {
            authRepository.isAuthAsFlow.collect { isAuth ->
                withContext(Dispatchers.Main) {
                    if (!isAuth && !hasFragmentInNavBackStack("LaunchFragment")) {
                        navController
                            .navigate(AppNavigationDirections.actionFromAuthorizedGraphToLaunchFragment())
                    }
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