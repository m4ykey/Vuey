package com.example.vuey.feature_album.presentation.viewmodel.ui_state

data class ArtistAlbumUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val artistAlbumData : ArtistDetail? = null
)