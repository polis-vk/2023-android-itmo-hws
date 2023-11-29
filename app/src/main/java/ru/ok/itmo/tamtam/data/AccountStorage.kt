package ru.ok.itmo.tamtam.data

import android.content.Context
import android.content.SharedPreferences
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.utils.ApplicationContext
import javax.inject.Inject


@AppComponentScope
class AccountStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val accountPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)

    var token: String?
        get() = accountPreferences.getString(PREF_KEY_TOKEN, null)
        set(value) {
            accountPreferences.edit().putString(PREF_KEY_TOKEN, value).apply()
        }

    var login: String?
        get() = accountPreferences.getString(PREF_KEY_LOGIN, null)
        set(value) {
            accountPreferences.edit().putString(PREF_KEY_LOGIN, value).apply()
        }

    fun clear() {
        accountPreferences.edit().remove(PREF_KEY_TOKEN).apply()
        accountPreferences.edit().remove(PREF_KEY_LOGIN).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "ru.ok.itmo.tamtam"
        private const val PREF_KEY_TOKEN = "token"
        private const val PREF_KEY_LOGIN = "login"
    }
}