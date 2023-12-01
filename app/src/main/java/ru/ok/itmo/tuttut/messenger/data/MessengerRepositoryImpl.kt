package ru.ok.itmo.tuttut.messenger.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import ru.ok.itmo.tuttut.LOGIN
import ru.ok.itmo.tuttut.TOKEN
import ru.ok.itmo.tuttut.messenger.domain.MessengerRepository
import ru.ok.itmo.tuttut.network.domain.Client
import javax.inject.Inject

class MessengerRepositoryImpl @Inject constructor(
    private val client: Client,
    private val dataStore: DataStore<Preferences>
) : MessengerRepository {
    private val api = client.create(MessengerAPI::class.java)
    override suspend fun getInbox(): Result<String> {
        return dataStore.data.first().let {
            val login =
                it[LOGIN] ?: return@let Result.failure(IllegalStateException("Login missed"))
            val token =
                it[TOKEN] ?: return@let Result.failure(IllegalStateException("Token missed"))
            client.safeRequest { api.inbox(token, login).toString() }
        }
    }
}