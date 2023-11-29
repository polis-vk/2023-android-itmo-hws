package ru.ok.itmo.tamtam.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.R

fun Context.observeNotifications(
    lifecycleScope: CoroutineScope,
    notifications: Channel<NotificationType>
) {
    lifecycleScope.launch {
        for (notification in notifications) {
            notification.printStackTrace()
            when (notification) {
                is NotificationType.Client ->
                    Toast.makeText(
                        this@observeNotifications,
                        getString(R.string.client_exception_message),
                        Toast.LENGTH_SHORT
                    ).show()

                is NotificationType.Connection -> Toast.makeText(
                    this@observeNotifications,
                    getString(R.string.connection_exception_message),
                    Toast.LENGTH_SHORT
                ).show()

                is NotificationType.Server -> Toast.makeText(
                    this@observeNotifications,
                    getString(R.string.server_exception_message),
                    Toast.LENGTH_SHORT
                ).show()

                is NotificationType.Unauthorized -> Toast.makeText(
                    this@observeNotifications,
                    getString(R.string.unauthorized_exception_message),
                    Toast.LENGTH_SHORT
                ).show()

                is NotificationType.Unknown -> Toast.makeText(
                    this@observeNotifications,
                    getString(R.string.unknown_exception_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}