package com.example.vuey.data.remote.response.tv_show

import com.example.vuey.data.models.tv_show.SearchTvShow

data class TvShowSearchResponse(
    val page: Int,
    val results: List<SearchTvShow>,
    val total_pages: Int,
    val total_results: Int
)