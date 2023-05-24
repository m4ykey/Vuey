package com.example.vuey.feature_album.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_album.data.database.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.Album
import com.example.vuey.feature_album.data.remote.model.AlbumDetailResponse
import com.example.vuey.feature_album.data.repository.AlbumRepository
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumSearch = MutableLiveData<Resource<List<Album>>>()
    val albumSearch: LiveData<Resource<List<Album>>> get() = _albumSearch

    private val _albumDetail = MutableLiveData<Resource<AlbumDetailResponse>>()
    val albumDetail: LiveData<Resource<AlbumDetailResponse>> get() = _albumDetail

    val getAllAlbums: LiveData<List<AlbumEntity>> = albumRepository.getAllAlbums()

    fun getAlbumById(albumId: String) : LiveData<AlbumEntity> {
        return albumRepository.getAlbumById(albumId)
    }

    fun insertAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            albumRepository.insertAlbum(albumEntity)
        }
    }

    fun deleteAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            albumRepository.deleteAlbum(albumEntity)
        }
    }

    fun getAlbum(albumId: String) {
        viewModelScope.launch {
            _albumDetail.value = Resource.Loading()
            try {
                val response = albumRepository.getAlbum(albumId)
                _albumDetail.value = response.data?.let { Resource.Success(it) }
            } catch (e: Exception) {
                _albumDetail.value =
                    Resource.Failure("Failed to fetch album list ${e.localizedMessage}")
            }
        }
    }

    fun searchAlbum(album: String) {
        viewModelScope.launch {
            _albumSearch.value = Resource.Loading()
            try {
                val response = albumRepository.searchAlbum(album)
                _albumSearch.value = response.data?.let { Resource.Success(it) }
            } catch (e: Exception) {
                _albumSearch.value =
                    Resource.Failure("Failed to fetch album list ${e.localizedMessage}")
            }
        }
    }
}