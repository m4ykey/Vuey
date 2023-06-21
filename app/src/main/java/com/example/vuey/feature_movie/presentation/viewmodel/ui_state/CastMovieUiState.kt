package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import com.example.vuey.feature_movie.data.remote.model.MovieCast

data class CastMovieUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val castMovieData : List<MovieCast.CastDetail> = emptyList()
)
