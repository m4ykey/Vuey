package com.example.vuey.feature_movie.presentation.viewmodel.ui_state

import com.example.vuey.feature_movie.data.remote.model.MovieList

data class SearchMovieUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val searchMovieData : List<MovieList> = emptyList()
)