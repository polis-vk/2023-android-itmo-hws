package com.example.settingsscreenitmoproject

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.settingsscreenitmoproject.ui.theme.AppTypography
import com.example.settingsscreenitmoproject.ui.theme.Secondary
import com.example.settingsscreenitmoproject.ui.theme.SettingsScreenItmoProjectTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val categories by viewModel.categoryFlow.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(title = {
            Text(
                text = "Название раздела",
                style = AppTypography.title17MediumBlack,
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 18.dp, end = 16.dp)
                    .width(275.dp)
            )
        },
            navigationIcon = {
                IconButton(
                    onClick = { //TODO::
                    },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 18.dp,
                        end = (2.5).dp,
                        bottom = 18.dp
                    )
                ) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Назад")
                }
            })

        LazyColumn {
            categories.forEachIndexed { categoryIndex, (title, settingSections) ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 16.dp, end = 24.dp)
                    ) {
                        Text(text = title, style = AppTypography.title12NormalAccent)
                    }
                }

                itemsIndexed(
                    settingSections,
                    key = { _, setting -> setting.settingId }) { settingsIndex, section ->
                    when (section) {
                        is Category.SettingSection.TouchSetting -> {
                            val localModifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
                            if (section.description.isNotBlank()) {
                                Column(modifier = localModifier) {
                                    Text(
                                        text = section.title,
                                        style = AppTypography.title16NormalBlack,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = section.description,
                                        style = AppTypography.title12NormalSecondary,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            } else {
                                Text(
                                    text = section.title,
                                    style = AppTypography.title16NormalBlack,
                                    modifier = localModifier
                                )
                            }
                        }

                        is Category.SettingSection.ToggleSetting -> {
                            var checked by rememberSaveable { mutableStateOf(section.enabled) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 24.dp,
                                        top = 16.dp,
                                        end = 24.dp,
                                        bottom = 16.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                val localModifier = Modifier
                                    .weight(1f)
                                    .padding(end = 3.dp)

                                if (section.description.isNotBlank()) {
                                    Column(modifier = localModifier) {
                                        Text(
                                            text = section.title,
                                            style = AppTypography.title16NormalBlack,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Text(
                                            text = section.description,
                                            style = AppTypography.title12NormalSecondary
                                        )
                                    }
                                } else {
                                    Text(
                                        text = section.title,
                                        style = AppTypography.title16NormalBlack,
                                        modifier = localModifier
                                    )
                                }


                                Switch(
                                    checked = checked,
                                    onCheckedChange = {
                                        checked = it
                                    }
                                )

                            }
                        }

                        else -> {}
                    }

                    if (settingsIndex == settingSections.size - 1 && categoryIndex != categories.size - 1) {
                        Divider(
                            color = Secondary,
                            thickness = 8.dp
                        )
                    } else if (categoryIndex == categories.size - 1 && settingsIndex == settingSections.size - 1) {
                        TextButton(
                            onClick = { /* TODO:: */ },
                            modifier = Modifier
                                .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp),
                            shape = RectangleShape,
                            contentPadding = PaddingValues(
                                start = 0.dp,
                                top = 0.dp,
                                bottom = 0.dp,
                                end = 0.dp
                            )
                        ) {
                            Text(
                                text = "Очистить кэш",
                                style = AppTypography.title16NormalRed,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

            }
        }
    }

}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreenItmoProjectTheme {
        SettingsScreen(viewModel = SettingsViewModel())
    }
}