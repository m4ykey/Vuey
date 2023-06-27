package com.example.vuey.feature_album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.repository.AlbumRepository
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.ArtistAlbumUiState
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.ArtistUiState
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.DetailAlbumUiState
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.SearchAlbumUiState
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumUseCases
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val useCases: AlbumUseCases,
    private val repository: AlbumRepository
) : ViewModel() {

    private val _albumSearchUiState = MutableStateFlow(SearchAlbumUiState())
    val albumSearchUiState : StateFlow<SearchAlbumUiState> get() = _albumSearchUiState

    private val _albumDetailUiState = MutableStateFlow(DetailAlbumUiState())
    val albumDetailUiState : StateFlow<DetailAlbumUiState> get() = _albumDetailUiState

    private val _searchAlbumInDatabase = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val searchAlbumInDatabase : StateFlow<List<AlbumEntity>> get() = _searchAlbumInDatabase

    private val _albumArtistUiState = MutableStateFlow(ArtistAlbumUiState())
    val albumArtistUiState : StateFlow<ArtistAlbumUiState> get() = _albumArtistUiState

    private val _artistUiState = MutableStateFlow(ArtistUiState())
    val artistUiState : StateFlow<ArtistUiState> get() = _artistUiState

    fun getTotalTracks() : Flow<Int> {
        return repository.getTotalTracks()
    }

    fun getTotalLength() : Flow<Int> {
        return repository.getTotalLength()
    }

    fun getAlbumCount() : Flow<Int> {
        return repository.getAlbumCount()
    }

    fun searchAlbumDatabase(albumName : String) {
        viewModelScope.launch {
            repository.searchAlbumInDatabase(albumName).collect { albumList ->
                _searchAlbumInDatabase.emit(albumList)
            }
        }
    }

    val allAlbums = repository.getAllAlbums()

    fun refreshDetail(albumId: String) {
        getAlbumDetail(albumId)
    }

    fun insertAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            repository.insertAlbum(albumEntity)
        }
    }

    fun deleteAlbum(albumEntity: AlbumEntity) {
        viewModelScope.launch {
            repository.deleteAlbum(albumEntity)
        }
    }

    fun getAlbumById(albumId : String) : Flow<AlbumEntity> {
        return repository.getAlbumById(albumId)
    }

    fun getArtistInfo(artistName : String) {
        useCases.getArtistUseCase(artistName).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _artistUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred",
                            artistData = result.data
                        )
                    }
                }
                is Resource.Success -> {
                    _artistUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            artistData = result.data
                        )
                    }
                }
                is Resource.Loading -> {
                    _artistUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            artistData = result.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getArtistDetail(artistId : String) {
        useCases.getAlbumArtistUseCase(artistId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _albumArtistUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred",
                            artistAlbumData = result.data
                        )
                    }
                }
                is Resource.Loading -> {
                    _albumArtistUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            artistAlbumData = result.data
                        )
                    }
                }
                is Resource.Success -> {
                    _albumArtistUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            artistAlbumData = result.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAlbumDetail(albumId : String) {
        useCases.getAlbumDetailUseCase(albumId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _albumDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            detailAlbumData = result.data
                        )
                    }
                }
                is Resource.Failure -> {
                    _albumDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred.",
                            detailAlbumData = result.data
                        )
                    }
                }
                is Resource.Loading -> {
                    _albumDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            detailAlbumData = result.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun searchAlbum(albumName : String) {
        useCases.getAlbumSearchUseCase(albumName).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _albumSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            searchAlbumData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Failure -> {
                    _albumSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred.",
                            searchAlbumData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Loading -> {
                    _albumSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            searchAlbumData = result.data ?: emptyList()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}