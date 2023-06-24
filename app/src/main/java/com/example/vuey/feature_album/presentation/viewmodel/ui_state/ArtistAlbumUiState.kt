package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import com.example.vuey.feature_album.data.remote.model.ArtistDetail

data class ArtistAlbumUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val artistAlbumData : ArtistDetail? = null
)