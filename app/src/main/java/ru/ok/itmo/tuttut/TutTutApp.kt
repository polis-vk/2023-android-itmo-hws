package ru.ok.itmo.tuttut

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TutTutApp : MultiDexApplication()

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val TOKEN = stringPreferencesKey("token")

val LOGIN = stringPreferencesKey("login")
val PASSWORD = stringPreferencesKey("password")