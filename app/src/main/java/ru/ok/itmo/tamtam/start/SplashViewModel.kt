package ru.ok.itmo.tamtam.start

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ok.itmo.tamtam.helper.Helper
import ru.ok.itmo.tamtam.token.AuthException
import ru.ok.itmo.tamtam.token.TokenRepository

class SplashViewModel : ViewModel(), KoinComponent {
    private val tokenRepositoryInstance: TokenRepository by inject()

    sealed class State {
        data class Success(val direction: NavDirections) : State()
        data class Error(val e: Exception) : State()
    }

    val state: MutableLiveData<State> = MutableLiveData()

    private fun sendToLogin() {
        viewModelScope.launch(Dispatchers.Main) {
            state.value = State.Success(
                SplashFragmentDirections.actionSplashFragmentToLoginNavGraph()
            )
        }
    }

    private fun sendToApp(channelsJson: String) {
        viewModelScope.launch(Dispatchers.Main) {
            state.value = State.Success(
                SplashFragmentDirections.actionSplashFragmentToAppNavGraph(channelsJson)
            )
        }
    }

    private suspend fun logout() {
        tokenRepositoryInstance.logout().collect { sendToLogin() }
    }

    private suspend fun checkAuthAndGetChannels() {
        tokenRepositoryInstance.checkAuthAndGetChannels().collect { sendToApp(it) }
    }

    fun getDirection(type: SplashType) {
        viewModelScope.launch {
            try {
                Log.d(Helper.DEBUG_TAG, "getDirection, type: $type")
                when (type) {
                    SplashType.LOGOUT_TYPE -> logout()
                    SplashType.CHECK_TYPE -> checkAuthAndGetChannels()
                }
            } catch (e: AuthException) {
                sendToLogin()
            } catch (e: Exception) {
                Log.d(Helper.DEBUG_TAG, "getDirection: $e")
                viewModelScope.launch(Dispatchers.Main) {
                    state.value = State.Error(e)
                }
            }
        }
    }
}