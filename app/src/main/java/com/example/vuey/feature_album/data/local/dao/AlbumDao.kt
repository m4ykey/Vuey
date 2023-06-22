package com.example.vuey.feature_album.data.local.dao

import androidx.room.*
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.local.entity.AlbumStatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album_table ORDER BY saveTime ASC")
    fun getAllAlbums() : Flow<List<AlbumEntity>>

    @Query("SELECT * FROM album_table WHERE id = :albumId")
    fun getAlbumById(albumId : String) : Flow<AlbumEntity>

    @Query("SELECT COUNT(*) FROM statistics_album_table_name")
    fun getAlbumCount() : Flow<Int>

    @Query("SELECT SUM(statisticsTotalTracks) FROM statistics_album_table_name")
    fun getTotalTracks() : Flow<Int>

    @Query("SELECT SUM(statisticsAlbumLength) FROM statistics_album_table_name")
    fun getTotalLength() : Flow<Int>

    @Query("SELECT COUNT(DISTINCT statisticsArtistList) FROM statistics_album_table_name")
    fun getTotalArtist() : Flow<Int>

    @Query("SELECT * FROM album_table WHERE albumName LIKE '%' ||:searchQuery||'%'")
    fun searchAlbumInDatabase(searchQuery: String) : Flow<List<AlbumEntity>>

    @Delete
    suspend fun deleteAlbum(albumEntity: AlbumEntity)

    @Delete
    suspend fun deleteAlbumStatistics(albumStatisticsEntity: AlbumStatisticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumStatistics(albumStatisticsEntity: AlbumStatisticsEntity)

}