package com.example.settingsscreenitmoproject

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val categories: List<Category> = listOf(
        Category(
            "Категория 1",
            listOf(
                Category.SettingSection.TouchSetting("Настройка времени", "Описание", 1),
                Category.SettingSection.ToggleSetting("Режим инкогнито", "Описание 2", false, 2)
            )
        ),
        Category(
            "Категория 2",
            listOf(
                Category.SettingSection.TouchSetting("Настройка жизни", "", 3),
                Category.SettingSection.ToggleSetting("Режим онлайн", "Описание 3", true, 4)
            )
        )

    )

    val categoryFlow: StateFlow<List<Category>> = MutableStateFlow(categories)
}

data class Category(
    val title: String,
    val settingSections: List<SettingSection>
) {
    sealed class SettingSection(val settingId: Int) {
        data class TouchSetting(val title: String, val description: String, val id: Int) :
            SettingSection(id)

        data class ToggleSetting(
            val title: String,
            val description: String,
            val enabled: Boolean,
            val id: Int
        ) :
            SettingSection(id)
    }
}