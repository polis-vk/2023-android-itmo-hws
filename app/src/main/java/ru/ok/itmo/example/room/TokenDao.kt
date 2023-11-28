package ru.ok.itmo.example.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import ru.ok.itmo.example.room.entities.TokenEntity

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tokenEntity: TokenEntity)

    @Update
    fun update(tokenEntity: TokenEntity)


    @Delete
    fun delete(tokenEntity: TokenEntity)

    @Query("SELECT token FROM token_table")
    fun getToken(): List<TokenEntity>;

}