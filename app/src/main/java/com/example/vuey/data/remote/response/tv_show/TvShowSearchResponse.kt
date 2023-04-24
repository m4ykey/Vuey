package com.example.vuey.data.remote.response.tv_show

import com.example.vuey.data.models.tv_show.SearchTvShow

data class TvShowSearchResponse(
    val results: List<SearchTvShow>
)