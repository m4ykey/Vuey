package com.example.vuey.feature_tv_show.data.response

import com.example.vuey.feature_tv_show.data.api.season.Episode

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