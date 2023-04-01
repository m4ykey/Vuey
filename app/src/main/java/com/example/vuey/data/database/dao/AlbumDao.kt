package com.example.vuey.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vuey.data.database.model.AlbumEntity
import retrofit2.http.DELETE

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

    @Query("SELECT * FROM album_table ORDER BY saveTime ASC")
    fun getAllAlbums() : LiveData<List<AlbumEntity>>

    @Delete
    suspend fun deleteAlbum(albumEntity: AlbumEntity)

}