package com.example.vuey.feature_movie.data.response

import com.example.vuey.feature_movie.data.api.detail.Genre
import com.example.vuey.feature_movie.data.api.detail.SpokenLanguage

data class MovieDetailResponse(
    val backdrop_path: String?,
    val genres: List<Genre>,
    val id: Int,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val title: String,
    val vote_average: Double,
)