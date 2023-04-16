package com.example.vuey.data.remote.response.movie

import com.example.vuey.data.models.movie.search.SearchMovie

data class MovieSearchResponse(
    val page: Int,
    val results: List<SearchMovie>,
    val total_pages: Int,
    val total_results: Int
)