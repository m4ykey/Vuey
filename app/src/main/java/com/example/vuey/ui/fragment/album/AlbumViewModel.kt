package com.example.vuey.ui.fragment.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.data.local.album.detail.AlbumDetail
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.data.repository.AlbumRepository
import com.example.vuey.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumSearch = MutableLiveData<Resource<List<Album>>>()
    val albumSearch : LiveData<Resource<List<Album>>> get() = _albumSearch

    private val _albumDetail = MutableLiveData<Resource<AlbumDetail>>()
    val albumDetail : LiveData<Resource<AlbumDetail>> get() = _albumDetail

    fun getInfo(artist : String, album: String) {
        viewModelScope.launch {
            _albumDetail.value = Resource.Loading()
            try {
                val response = albumRepository.getInfo(artist, album)
                _albumDetail.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _albumDetail.value = Resource.Failure("Failed to fetch album detail ${e.localizedMessage}")
            }
        }
    }

    fun searchAlbum(album : String) {
        viewModelScope.launch {
            _albumSearch.value = Resource.Loading()
            try {
                val response = albumRepository.searchAlbum(album)
                _albumSearch.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _albumSearch.value = Resource.Failure("Failed to fetch album list ${e.localizedMessage}")
            }
        }
    }
}