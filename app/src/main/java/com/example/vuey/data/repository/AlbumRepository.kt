package com.example.vuey.data.repository

import androidx.lifecycle.LiveData
import com.example.vuey.data.local.album.detail.AlbumDetail
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.data.local.database.dao.AlbumDao
import com.example.vuey.data.local.database.model.AlbumEntity
import com.example.vuey.data.remote.api.AlbumApi
import com.example.vuey.util.Resource
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao
) {

    suspend fun insertAlbum(albumEntity: AlbumEntity) = albumDao.insertAlbum(albumEntity)
    suspend fun deleteAlbum(albumEntity: AlbumEntity) = albumDao.deleteAlbum(albumEntity)
    fun getAllAlbums() : LiveData<List<AlbumEntity>> = albumDao.getAllAlbums()
    //fun deleteAllAlbums() : LiveData<List<AlbumEntity>> = albumDao.deleteAllAlbums()

    suspend fun getInfo(artist : String, album : String) : Resource<AlbumDetail> {
        return try {
            val response = albumApi.getInfo(artist, album)
            if (response.isSuccessful) {
                val albumDetailResponse = response.body()
                val albumDetail = albumDetailResponse!!.album
                Resource.Success(albumDetail)
            } else {
                Resource.Failure("Failed to fetch album detail")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch album detail ${e.localizedMessage}")
        }
    }
    suspend fun searchAlbum(albumName: String): Resource<List<Album>> {
        return try {
            val response = albumApi.searchAlbum(albumName)
            if (response.isSuccessful) {
                val albumSearchResponse = response.body()
                val albums = albumSearchResponse?.results?.albummatches!!.album
                Resource.Success(albums)
            } else {
                Resource.Failure("Failed to fetch album list")
            }
        } catch (e: Exception) {
            Resource.Failure("Failed to fetch album list ${e.localizedMessage}")
        }
    }
}