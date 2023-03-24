package com.example.vuey.data.repository

import com.example.vuey.data.local.album.search.Album
import com.example.vuey.data.remote.api.AlbumApi
import com.example.vuey.util.Resource
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi
) {
    suspend fun searchAlbum(albumName: String): Resource<List<Album>> {
        return try {
            val response = albumApi.searchAlbum(albumName)
            if (response.isSuccessful) {
                val albumSearchResponse = response.body()
                val albums = albumSearchResponse!!.results.albummatches.album
                Resource.Success(albums)
            } else {
                Resource.Failure("Failed to fetch album list")
            }
        } catch (e: Exception) {
            Resource.Failure("Failed to fetch album list ${e.localizedMessage}")
        }
    }
}