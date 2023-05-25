package com.example.vuey.feature_album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_album.ui_state.AlbumDetailUiState
import com.example.vuey.feature_album.ui_state.SearchAlbumUiState
import com.example.vuey.feature_album.use_cases.UseCases
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _albumSearchUiState = MutableStateFlow(SearchAlbumUiState())
    val albumSearchUiState : StateFlow<SearchAlbumUiState> get() = _albumSearchUiState

    private val _albumDetailUiState = MutableStateFlow(AlbumDetailUiState())
    val albumDetailUiState : StateFlow<AlbumDetailUiState> get() = _albumDetailUiState

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