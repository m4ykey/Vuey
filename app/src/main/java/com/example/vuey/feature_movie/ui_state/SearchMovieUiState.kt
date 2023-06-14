package com.example.vuey.feature_movie.ui_state

import com.example.vuey.feature_movie.data.remote.model.SearchMovie

data class SearchMovieUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val searchMovieData : List<SearchMovie> = emptyList()
)