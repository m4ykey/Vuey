package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import com.example.vuey.feature_movie.data.remote.model.MovieDetail

data class DetailMovieUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val detailMovieData : MovieDetail? = null
)