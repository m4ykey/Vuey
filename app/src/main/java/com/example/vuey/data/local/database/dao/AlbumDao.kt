package com.example.vuey.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.data.local.database.model.AlbumEntity

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

    @Query("select * from album_table order by saveAt desc ")
    fun getAllAlbums() : LiveData<List<AlbumEntity>>

    @Delete
    suspend fun deleteAlbum(albumEntity: AlbumEntity)

}