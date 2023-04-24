package com.example.vuey.data.remote.response.movie

import com.example.vuey.data.models.movie.detail.Cast

data class MovieTvShowCreditsResponse(
    val cast: List<Cast>
)