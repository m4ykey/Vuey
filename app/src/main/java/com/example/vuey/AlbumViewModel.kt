package com.example.vuey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.model.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumResponse = MutableLiveData<List<Album>>()
    val albumResponse : LiveData<List<Album>> get() = _albumResponse

    fun searchAlbum(album : String) = viewModelScope.launch {
        albumRepository.searchAlbum(album).let {
            if (it.isSuccessful) {
                _albumResponse.value = it.body()!!.results.albummatches.album
            }
        }
    }
}