package com.example.vuey.feature_album.presentation.viewmodel.ui_state

data class ArtistTopTracksUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val artistTopTracksData : List<Track> = emptyList()
)
