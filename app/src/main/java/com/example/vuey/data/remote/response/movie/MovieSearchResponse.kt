package com.example.vuey.data.remote.response.movie

import com.example.vuey.data.models.movie.search.SearchMovie

data class MovieSearchResponse(
    val results: List<SearchMovie>
)