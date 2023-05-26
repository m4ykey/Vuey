package com.example.vuey.feature_album.data.local.dao

import androidx.room.*
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album_table ORDER BY saveTime ASC")
    fun getAllAlbums() : Flow<List<AlbumEntity>>

    @Query("SELECT * FROM album_table WHERE id = :albumId")
    fun getAlbumById(albumId : String) : Flow<AlbumEntity>

    @Delete
    suspend fun deleteAlbum(albumEntity: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

}