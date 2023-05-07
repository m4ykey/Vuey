package com.example.vuey.feature_movie.data.response

import com.example.vuey.feature_movie.data.api.search.SearchMovie

data class MovieSearchResponse(
    val results: List<SearchMovie>
)