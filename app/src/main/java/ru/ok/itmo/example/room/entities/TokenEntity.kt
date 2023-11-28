package ru.ok.itmo.example.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_table")
data class TokenEntity (
    @PrimaryKey val id: Long? = 0,
    @ColumnInfo(name = "token") val token: String?
)