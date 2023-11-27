package ru.ok.itmo.tamtam.login.login_fragment

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ok.itmo.tamtam.server.ServerWorker
import ru.ok.itmo.tamtam.token.TokenRepository

class LoginModel : KoinComponent {
    private val tokenRepositoryInstance: TokenRepository by inject()

    suspend fun authorization(
        login: String,
        password: String,
        onActionCompleted: (Unit) -> Unit
    ) {
        ServerWorker.login(login, password).collect { token ->
            tokenRepositoryInstance.saveToken(token)
            onActionCompleted(Unit)
        }
    }
}