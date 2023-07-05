package com.example.vuey.feature_album.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_album.presentation.viewmodel.ui_state.ArtistUiState
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.ArtistUseCase
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistUseCase : ArtistUseCase
) : ViewModel() {

    private val _artistUiState = MutableStateFlow(ArtistUiState())
    val artistUiState : StateFlow<ArtistUiState> get() = _artistUiState

    fun getArtistInfo(artistName : String) {
        artistUseCase(artistName).onEach { result ->
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

}