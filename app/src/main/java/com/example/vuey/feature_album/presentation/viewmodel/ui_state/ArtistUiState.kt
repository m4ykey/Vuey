package com.example.vuey.feature_album.presentation.viewmodel.ui_state

import com.example.vuey.feature_album.data.remote.model.last_fm.Artist

data class ArtistUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val artistData : Artist? = null
)