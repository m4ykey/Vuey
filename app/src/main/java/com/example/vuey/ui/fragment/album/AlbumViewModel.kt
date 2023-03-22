package com.example.vuey.ui.fragment.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.data.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumResponse = MutableLiveData<List<Album>>()
    val albumResponse : LiveData<List<Album>> get() = _albumResponse

    private val _albumDetail = MutableLiveData<com.example.vuey.data.local.album.detail.AlbumDetail>()
    val albumDetail : LiveData<com.example.vuey.data.local.album.detail.AlbumDetail> get() = _albumDetail

    fun searchAlbum(album : String) = viewModelScope.launch {
        albumRepository.searchAlbum(album).let {
            if (it.isSuccessful) {
                _albumResponse.value = it.body()!!.results.albummatches.album
            }
        }
    }

    fun getInfo(artist: String, album : String) = viewModelScope.launch {
        albumRepository.getInfo(album, artist).let {
            if (it.isSuccessful) {
                _albumDetail.value = it.body()!!.album
            }
        }
    }
}