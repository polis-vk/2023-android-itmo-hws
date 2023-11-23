package ru.ok.itmo.example.model

import org.json.JSONObject

data class Message(
    val id: Long,
    val from: String,
    val to: String?,
    val data: Data,
    val time: Long
) {
    companion object {
        fun parseJSON(json: JSONObject): Message {
            return Message(
                json.getLong("id"),
                json.getString("from"),
                json.getString("to"),
                json.getJSONObject("data").parseData(),
                json.getLong("time")
            )
        }

        private fun JSONObject.parseData(): Data {
            return Data(
                optJSONObject("Text")?.getString("text"),
                optJSONObject("Image")?.getString("link")
            )
        }
    }

    data class Data(
        val text: String?,
        val image: String?
    )
}

