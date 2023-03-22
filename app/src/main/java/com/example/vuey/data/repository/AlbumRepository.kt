package com.example.vuey.data.repository

import com.example.vuey.data.remote.api.AlbumApi
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi
){
    suspend fun searchAlbum(albumName : String) = albumApi.searchAlbum(albumName)
    suspend fun getInfo(artistName : String, albumName : String) = albumApi.getInfo(albumName, artistName)
}