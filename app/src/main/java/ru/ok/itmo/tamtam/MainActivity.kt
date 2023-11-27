package ru.ok.itmo.tamtam

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.ok.itmo.tamtam.shared_model.SharedViewModel
import ru.ok.itmo.tamtam.token.TokenModel
import ru.ok.itmo.tamtam.token.TokenRepository

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val koinApp = startKoin {
                androidContext(this@MainActivity)
                modules(appModule)
            }.koin

            val tokenRepository: TokenRepository by koinApp.inject()

            tokenRepository.init(this)
        }

        sharedViewModel.navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
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

val appModule = module {
    single { TokenRepository() }
    single<TokenModel> {
        get<TokenRepository>()
    }
}