package com.example.vuey.feature_album.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vuey.feature_album.data.local.entity.AlbumEntity

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album_table ORDER BY saveTime ASC")
    fun getAllAlbums() : LiveData<List<AlbumEntity>>

    @Query("SELECT * FROM album_table WHERE id = :albumId")
    fun getAlbumById(albumId : String) : LiveData<AlbumEntity>

    @Delete
    suspend fun deleteAlbum(albumEntity: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

}