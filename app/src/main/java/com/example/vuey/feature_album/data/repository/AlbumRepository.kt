package com.example.vuey.feature_album.data.repository

import androidx.lifecycle.LiveData
import com.example.vuey.feature_album.data.database.dao.AlbumDao
import com.example.vuey.feature_album.data.database.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.feature_album.data.remote.model.Album
import com.example.vuey.feature_album.data.remote.model.AlbumDetailResponse
import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.util.network.Resource
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val spotifyInterceptor: SpotifyInterceptor
) {

    suspend fun insertAlbum(albumEntity: AlbumEntity) = albumDao.insertAlbum(albumEntity)
    suspend fun deleteAlbum(albumEntity: AlbumEntity) = albumDao.deleteAlbum(albumEntity)
    fun getAllAlbums() : LiveData<List<AlbumEntity>> = albumDao.getAllAlbums()
    fun getAlbumById(albumId: String) : LiveData<AlbumEntity> = albumDao.getAlbumById(albumId)

    suspend fun getAlbum(albumId : String) : Resource<AlbumDetailResponse> {
        return try {
            val response = albumApi.getAlbum(
                token = "Bearer ${spotifyInterceptor.getAccessToken()}",
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
                token = "Bearer ${spotifyInterceptor.getAccessToken()}",
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