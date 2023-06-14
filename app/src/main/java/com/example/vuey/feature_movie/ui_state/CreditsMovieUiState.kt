package com.example.vuey.feature_movie.ui_state

import com.example.vuey.feature_movie.data.remote.model.Cast

data class CreditsMovieUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val movieCreditsData : List<Cast> = emptyList()
)