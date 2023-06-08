package com.example.vuey.feature_album.data.repository

import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.Album
import com.example.vuey.feature_album.data.remote.model.AlbumDetail
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun searchAlbum(albumName : String) : Flow<Resource<List<Album>>>
    fun getAlbum(albumId : String) : Flow<Resource<AlbumDetail>>

    suspend fun insertAlbum(albumEntity: AlbumEntity)
    suspend fun deleteAlbum(albumEntity: AlbumEntity)
    fun getAllAlbums() : Flow<List<AlbumEntity>>
    fun getAlbumById(albumId : String) : Flow<AlbumEntity>
    fun getAlbumCount() : Flow<Int>
    fun getTotalTracks() : Flow<Int>
    fun getTotalLength() : Flow<Int>

}