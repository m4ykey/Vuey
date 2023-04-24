package com.example.vuey.data.remote.response.tv_show

import com.example.vuey.data.models.tv_show.Episode

data class TvShowSeasonResponse(
    val _id: String,
    val air_date: String,
    val episodes: List<Episode>,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val season_number: Int
)