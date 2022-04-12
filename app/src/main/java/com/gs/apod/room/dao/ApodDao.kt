package com.gs.apod.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gs.apod.room.entity.FavouriteApods
import kotlinx.coroutines.flow.Flow

@Dao
interface ApodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavApod(apod: FavouriteApods)

    @Transaction
    @Query("SELECT * FROM favourite_apods")
    fun getAllFavApods() : Flow<List<FavouriteApods>>

    @Query("DELETE FROM favourite_apods WHERE url = :url")
    fun deleteByUrl(url: String)

    @Query("SELECT * FROM favourite_apods WHERE url = :url")
    fun getByUrl(url: String) : FavouriteApods?

    @Query("DELETE FROM favourite_apods")
    suspend fun deleteAll()
}