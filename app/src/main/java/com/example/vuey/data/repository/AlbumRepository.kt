package com.example.vuey.data.repository

import androidx.lifecycle.LiveData
import com.example.vuey.data.database.dao.AlbumDao
import com.example.vuey.data.database.model.AlbumEntity
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.data.remote.api.AlbumApi
import com.example.vuey.data.remote.api.SpotifyAuthInterceptor
import com.example.vuey.data.remote.response.AlbumDetailResponse
import com.example.vuey.util.Resource
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi,
    private val authInterceptor: SpotifyAuthInterceptor,
) {

    suspend fun getAlbum(albumId : String) : Resource<AlbumDetailResponse> {
        return try {
            val response = albumApi.getAlbum(
                token = "Bearer ${authInterceptor.getAccessToken()}",
                albumId = albumId
            )
            if (response.isSuccessful) {
                val albumDetail = response.body()
                val album = albumDetail!!
                Resource.Success(album)
            } else {
                Resource.Failure("Failed to fetch album details")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch album details ${e.localizedMessage}")
        }
    }

    suspend fun searchAlbum(albumName: String): Resource<List<Album>> {
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