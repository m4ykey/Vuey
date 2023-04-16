package com.example.vuey.data.remote.response.movie

import com.example.vuey.data.models.movie.detail.Cast

data class MovieCreditsResponse(
    val cast: List<Cast>,
    val id: Int
)