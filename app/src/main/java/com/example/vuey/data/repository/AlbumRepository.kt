package com.example.vuey.data.repository

import com.example.vuey.data.local.album.Item
import com.example.vuey.data.remote.api.AlbumApi
import com.example.vuey.data.remote.api.SpotifyAuthInterceptor
import com.example.vuey.util.Resource
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi,
    private val authInterceptor: SpotifyAuthInterceptor
) {


    suspend fun searchAlbum(albumName: String): Resource<List<Item>> {
        return try {
            val response = albumApi.searchAlbum(
                token = "Bearer ${authInterceptor.getAccessToken()}",
                query = albumName
            )
            if (response.isSuccessful) {
                val albumSearchResponse = response.body()
                val albums = albumSearchResponse!!.albums.items
                Resource.Success(albums)
            } else {
                Resource.Failure("Failed to fetch album list")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch album list ${e.localizedMessage}")
        }
    }
}