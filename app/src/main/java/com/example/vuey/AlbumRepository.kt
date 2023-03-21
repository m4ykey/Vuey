package com.example.vuey

import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi
){
    suspend fun searchAlbum(albumName : String) = albumApi.searchAlbum(albumName)
}