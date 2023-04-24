package com.example.vuey.data.remote.response.movie

import com.example.vuey.data.models.movie.detail.Genre
import com.example.vuey.data.models.movie.detail.SpokenLanguage

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
    val vote_count: Int
)