package com.example.vuey.feature_album.ui_state

import com.example.vuey.feature_album.data.remote.model.AlbumDetail

data class AlbumDetailUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val detailAlbumData : AlbumDetail? = null
)